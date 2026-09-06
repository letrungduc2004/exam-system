package com.example.exam_system.features.exam.service;

import com.example.exam_system.common.enums.EXAMTYPE;
import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.exam.dto.request.ExamRequest;
import com.example.exam_system.features.exam.dto.response.*;
import com.example.exam_system.features.exam.entity.Exam;
import com.example.exam_system.features.exam.mapper.ExamDetailMapping;
import com.example.exam_system.features.exam.mapper.ExamMapping;
import com.example.exam_system.features.exam.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.domain.PageRequest.of;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final ExamPartRepository examPartRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final ExamCommentRepository examCommentRepository;
    private final ExamMapping examMapping;
    private final ExamDetailMapping examDetailMapping;

    @PreAuthorize("hasRole('STUDENT')")
    @Transactional(readOnly = true)
    public Page<ExamResponse> getExam(List<String> examType, List<Integer> difficulty, List<String> level,
                                      Double priceFrom, Double priceTo, Pageable pageable) {
        // Kiểm tra trường hợp có examType nhưng value empty
        if (examType != null && examType.isEmpty()) {
            examType = null;
        }
        if (difficulty != null && difficulty.isEmpty()) {
            difficulty = null;
        }
        if (level != null && level.isEmpty()) {
            level = null;
        }
        Page<Exam> exam = examRepository.getExam(examType, difficulty, level, priceFrom, priceTo, pageable);
        List<ExamResponse> examConvert = examMapping.toListExamResponse(exam.getContent());
        return new PageImpl<>(examConvert, exam.getPageable(), exam.getTotalElements());
    }

    // SỬAAAAAAAAA
    public ExamResponse createExam(ExamRequest request) {
        boolean existingExam = examRepository.existsByTitle(request.getTitle());
        if (existingExam) {
            throw new AppException(ErrorCode.EXAM_NOT_FOUND);
        }
        Exam examConvert = examMapping.toExam(request);
        examConvert.setCreatedAt(LocalDateTime.now());
        examConvert.setUserCount(0L);
        examConvert.setStart(5.0);
        examRepository.save(examConvert);
        return examMapping.toExamResponse(examConvert);
    }

    @Transactional(rollbackFor = Exception.class)
    public ExamResponse updateExam(UUID publicExamId, ExamRequest request) {
        Exam existingExam = examRepository.findByPublicId(publicExamId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        // existingExam.setTitle(request.getTitle());
        // existingExam.setExamType(request.getExamType());
        // existingExam.setDuration(request.getDuration());
        // existingExam.setDifficulty(request.getDifficulty());
        // existingExam.setPrice(request.getPrice());
        // existingExam.setNumberTimes(request.getNumberTimes());
        // existingExam.setTotalQuestion(request.getTotalQuestion());
        // existingExam.setDiscription(request.getDiscription());
        examMapping.toUpdateExam(existingExam, request);
        examRepository.save(existingExam);
        return examMapping.toExamResponse(existingExam);
    }

    @Transactional(rollbackFor = Exception.class)
    public ExamResponse deleteExam(UUID publicExamId) {
        Exam existingExam = examRepository.findByPublicId(publicExamId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        examRepository.deleteById(existingExam.getId());
        return examMapping.toExamResponse(existingExam);
    }

    public Exam existingExam(UUID publicExamId) {
        Exam exam = examRepository.findByPublicId(publicExamId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));
        return exam;
    }

    public ExamFieldResponse getQuestionTest(UUID publicExamId, List<Long> idCurrentPart) {
        // Tư tưởng: Lấy toàn bộ câu hỏi trong bài thi
        // 1 Query lấy ra thông tin bài thi
        // 1 Query lấy ra Part - 1 Query Question + Option tương ứng mỗi Part
        // Mapping lần lượt Part - Question tương ứng

        ExamFieldResponse exam = examRepository.getExamForAttempt(publicExamId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));
        List<PartFieldResponse> examPart = examPartRepository.getAllExamPart(idCurrentPart);
        List<QuestionFieldResponse> question = questionRepository.getQuestionByPartId(idCurrentPart);
        List<Long> questionId = question.stream()
                .map(queId -> queId.getQuestionId())
                .collect(Collectors.toList());
        List<OptionFieldResponse> option = optionRepository.getOptionByQuestionId(questionId);

        // Mapping Option vào Question: questionId, List<Option>
        Map<Long, List<OptionFieldResponse>> mappingOption = new HashMap<>();
        for (OptionFieldResponse optionReponse : option) {
            Long quesId = optionReponse.getQuestionId();
            List<OptionFieldResponse> opt = mappingOption.get(quesId);
            if (Objects.isNull(opt)) {
                opt = new ArrayList<>();
                opt.add(optionReponse);
                mappingOption.put(quesId, opt);
            } else {
                opt.add(optionReponse);
            }
        }
        for (QuestionFieldResponse questionResponse : question) {
            Long queId = questionResponse.getQuestionId();
            List<OptionFieldResponse> op = mappingOption.get(queId);
            questionResponse.setOptionList(op);
        }
        // Mapping Question vào Part: partId, list<question>
        Map<Long, List<QuestionFieldResponse>> mappingPart = new HashMap<>();
        for (QuestionFieldResponse questionResponse : question) {
            Long paId = questionResponse.getPartId();
            List<QuestionFieldResponse> ques = mappingPart.get(paId);
            if (Objects.isNull(ques)) {
                ques = new ArrayList<>();
                ques.add(questionResponse);
                mappingPart.put(paId, ques);
            } else {
                ques.add(questionResponse);
            }
        }

        for (PartFieldResponse examPartResponse : examPart) {
            Long partId = examPartResponse.getPartId();
            List<QuestionFieldResponse> que = mappingPart.get(partId);
            examPartResponse.setQuestionList(que);
        }
        exam.setMultiPart(false);
        if (exam.getExamType().equals(EXAMTYPE.JLPT.name())) {
            exam.setMultiPart(true);
        }
        exam.setPartList(examPart);
        return exam;
    }


    @Transactional(readOnly = true)
    public ExamDetailResponse getExamDetailInformation(UUID publicExamId) {
        // Tư tưởng: lấy ra thông tin bài thi
        // Lấy ra thông tin part trong bài thi
        // Lấy ra các bài thi liên quan (random)
        // Lấy ra danh sách đánh giá của người dùng
        Exam examEntity = examRepository.findByPublicId(publicExamId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));
        ExamDetailResponse examDetail = examDetailMapping.toExamDetailResponse(examEntity);

        // Lấy ra thông tin part trong bài thi
        examDetail.setParts(examPartRepository.getExamPartSummariesByPublicId(publicExamId));

        // Lấy ra các bài thi liên quan
        List<Exam> relatedExamEntities = examRepository.getRandomRelatedExams(publicExamId, of(0, 3));
        examDetail.setRelatedExams(examDetailMapping.toRelatedExamResponseList(relatedExamEntities));

        // Lấy ra danh sách đánh giá của người dùng
        examDetail.setComment(examCommentRepository.getCommentsByExamPublicId(publicExamId));
        return examDetail;
    }

    @Transactional(readOnly = true)
    public ExamHistoryResponse getExamHistoryInformation(UUID publicExamId) {
        // Tư tưởng: lấy ra thông tin bài thi
        // Lấy ra thông tin part trong bài thi
        // Lấy ra các câu hỏi trong bài thi (isExplaining)
        // Lấy ra đáp án trong bài thi (isCorrect)
        ExamHistoryResponse exam = examRepository.getExamForHistory(publicExamId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        // Lấy ra thông tin part trong bài thi
        List<PartHistoryResponse> part = examPartRepository.getPartHistory(exam.getPublicExamId());
        List<Long> partIdList = part.stream().map(x -> x.getPartId()).collect(Collectors.toList());

        // Lấy ra các câu hỏi trong bài thi
        List<QuestionHistoryResponse> question = questionRepository.getQuestionForHistory(partIdList);
        List<Long> quesIdList = question.stream().map(x -> x.getQuestionId()).collect(Collectors.toList());

        // Lấy ra các đáp án tương ứng trong bài thi
        List<OptionHistoryResponse> option = optionRepository.getOptionForHistory(quesIdList);

        // Mapping Option vào Question => Map<questionId, List<Option>>
        Map<Long, List<OptionHistoryResponse>> optionMap = new HashMap<>();
        for (OptionHistoryResponse response : option) {
            Long quesId = response.getQuestionId();
            List<OptionHistoryResponse> value = optionMap.get(quesId);
            if (Objects.isNull(value)){
                value = new ArrayList<>();
                value.add(response);
                optionMap.put(quesId,value);
            }else{
                value.add(response);
            }
        }

        for(QuestionHistoryResponse response : question){
            Long quesId = response.getQuestionId();
            List<OptionHistoryResponse> value = optionMap.get(quesId);
            response.setOptionList(value);
        }

        // Mapping Question vào Part => Map<partId, List<QuestionHistoryResponse>>
        Map<Long, List<QuestionHistoryResponse>> questionMap = new HashMap<>();
        for(QuestionHistoryResponse response : question){
            Long partId = response.getPartId();
            List<QuestionHistoryResponse> value = questionMap.get(partId);
            if(Objects.isNull(value)){
                value = new ArrayList<>();
                value.add(response);
                questionMap.put(partId, value);
            }else{
                value.add(response);
            }
        }

        for(PartHistoryResponse response : part){
            Long partId = response.getPartId();
            List<QuestionHistoryResponse> value = questionMap.get(partId);
            response.setQuestion(value);
        }

        exam.setPart(part);
        // Lấy ra danh sách đánh giá của người dùng
        return exam;
    }

}

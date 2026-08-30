package com.example.exam_system.features.exam.repository;

import com.example.exam_system.features.exam.dto.response.OptionFieldResponse;
import com.example.exam_system.features.exam.dto.response.OptionHistoryResponse;
import com.example.exam_system.features.exam.entity.Option;
import com.example.exam_system.features.exam.repository.projection.QuestionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
// BJT
    // lấy các option dựa vào list questionId
    @Query("select new com.example.exam_system.features.exam.dto.response.OptionFieldResponse(o.id, o.content,  o.question.id) " +
            "from Option o " +
            "where o.question.id in :questionId")
    List<OptionFieldResponse> getOptionByQuestionId(@Param("questionId") List<Long> questionId);

    // Lấy các question, optionId(đáp án đúng) theo examId ở bảng Option -- done
    @Query("select o.id as optionId, ques.id as questionId , ques.scoreWeight as scoreWeight " +
            "from Option o " +
            "join o.question ques " +
            "join ques.examPart exPart " +
            "where exPart.exam.id =:examId and isCorrect = true ")
    List<QuestionProjection> getQuestionByExamId(Long examId);

// JLPT
    // Lấy các question, optionId(đáp án đúng) theo partId ở bảng Option -- done
    @Query("select o.id as optionId, ques.id as questionId , ques.scoreWeight as scoreWeight " +
            "from Option o " +
            "join o.question ques " +
            "join ques.examPart exPart " +
            "where exPart.id =:partId and isCorrect = true ")
     List<QuestionProjection> getQuestionByPartId(Long partId);


//    public class OptionHistoryResponse {
//        private Long optionId;
//        private String optionContent;
//        private boolean isCorrect;
    @Query("select new com.example.exam_system.features.exam.dto.response.OptionHistoryResponse(o.id, o.content, o.isCorrect ,o.question.id) " +
            "from Option o " +
            "where o.question.id in :questionId")
    List<OptionHistoryResponse> getOptionForHistory(@Param("questionId") List<Long> questionId);
}

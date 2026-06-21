package com.example.exam_system.features.exam_attempt.repository;

import com.example.exam_system.features.exam_attempt.entity.StudentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentResponseRepository extends JpaRepository<StudentResponse, Long> {

    // Lấy danh sách câu hỏi mã student đã làm trong 1 phiên thi
    @Query("select st from StudentResponse st where " +
            "st.examAttempt.id =:attemptId " +
            "and st.question.id in :questionId")
    List<StudentResponse> findQuestion(Long attemptId, List<Long> questionId);

    // Lấy toàn bộ câu hỏi student đã làm trong đề thi
    @Query("select st from StudentResponse st where " +
            "st.examAttempt.id =:attemptId ")
    List<StudentResponse> findQuestionByAttempt(Long attemptId);
}

# HƯỚNG DẪN KIẾN TRÚC MỞ RỘNG HỆ THỐNG THI (EXAM SYSTEM ARCHITECTURE DOCUMENT)

Tài liệu này đóng vai trò là **Blueprint Kiến trúc** giúp hệ thống dễ dàng mở rộng từ các bài thi hiện tại (BJT, JLPT) sang nhiều loại chứng chỉ đa ngôn ngữ khác (TOEIC, IELTS, HSK, TOPIK...) mà không vi phạm nguyên lý SOLID và không làm phá vỡ các đoạn code cũ.

---

## 🎯 1. NGUYÊN LÝ KIẾN TRÚC CỐT LÕI

Hệ thống được thiết kế theo sự kết hợp của 4 Design Pattern kinh điển:
1. **Strategy Pattern:** Đóng gói thuật toán/logic thi riêng biệt cho từng chứng chỉ.
2. **Composite Strategy (Modular Sub-Strategies):** Chia nhỏ bài thi thành 3 Trục độc lập: **Xáo Trộn (Shuffling)**, **Luồng Thi (Workflow)**, **Chấm Điểm (Grading)**.
3. **Factory Pattern:** Tự động tạo và quản lý các Strategy Instance theo `examType`.
4. **Facade Pattern:** Interface `ExamStrategy` giữ vai trò làm cổng giao tiếp hợp nhất duy nhất cho tầng Service, che giấu độ phức tạp của 3 Trục bên dưới.

### Tuân thủ Nguyên lý SOLID:
- **SRP (Single Responsibility Principle):** Mỗi Strategy con chỉ chịu trách nhiệm đúng 1 việc. `ExamAttemptService` chỉ làm nhiệm vụ điều phối (Orchestrator).
- **OCP (Open/Closed Principle):** Khi mở rộng bài thi mới (ví dụ TOEIC/IELTS), chỉ cần **thêm Class mới**, tuyệt đối **không sửa Class cũ**.
- **ISP (Interface Segregation Principle):** Tách nhỏ các interface `ExamWorkflowStrategy`, `GradingStrategy`, `ExamShufflerStrategy`.

---

## 🗺️ 2. SƠ ĐỒ 1: CẤU TRÚC TỔ CHỨC CLASS & MỐI QUAN HỆ IMPLEMENTATION ĐẦY ĐỦ

```text
                            ┌───────────────────────────────┐
                            │      ExamAttemptService       │ (Service điều phối chính)
                            └───────────────┬───────────────┘
                                            │
                                            ▼
                            ┌───────────────────────────────┐
                            │          ExamFactory          │ (Factory lấy Strategy theo ExamType)
                            └───────────────┬───────────────┘
                                            │
                                            ▼
                            ┌───────────────────────────────┐
                            │         ExamStrategy          │ <--- INTERFACE CHÍNH (Cổng giao tiếp Facade)
                            └───────────────┬───────────────┘
                                            │
        ┌───────────────────────────────────┼───────────────────────────────────┐
        │ implements                        │ implements                        │ implements
        ▼                                   ▼                                   ▼
┌──────────────────┐               ┌──────────────────┐               ┌──────────────────┐
│    BJTPattern    │               │   JLPTPattern    │               │   TOEICPattern   │ ... (HSK, TOPIK)
└────────┬─────────┘               └────────┬─────────┘               └────────┬─────────┘
         │                                  │                                  │
         └──────────────────────────────────┼──────────────────────────────────┘
                                            │ Tái sử dụng các Module Strategy con ở 3 Trục độc lập
                                            ▼
┌──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                             CÁC STRATEGY CON PHỤ TRỢ                                                 │
│                                                                                                                      │
│   [TRỤC 1: XÁO TRỘN - SHUFFLING]         [TRỤC 2: LUỒNG THI - WORKFLOW]           [TRỤC 3: TÍNH ĐIỂM - GRADING]      │
│   ┌────────────────────────────┐         ┌───────────────────────────┐            ┌───────────────────────────┐      │
│   │    ExamShufflerStrategy    │         │   ExamWorkflowStrategy    │            │      GradingStrategy      │      │
│   └─────────────┬──────────────┘         └─────────────┬─────────────┘            └─────────────┬─────────────┘      │
│                 │                                      │                                        │                    │
│      ┌──────────┼──────────┐                ┌──────────┴──────────┐               ┌─────────────┼─────────────┐      │
│      ▼          ▼          ▼                ▼                     ▼               ▼             ▼             ▼      │
│ ┌─────────┐ ┌─────────┐ ┌─────────┐    ┌──────────────────┐ ┌──────────────────┐┌────────────┐ ┌────────────┐ ┌────────────┐│
│ │NoOp     │ │Full     │ │Option   │    │ContinuousWorkflow│ │SectionalWorkflow ││RawScore    │ │ScaledScore │ │AsyncHybrid ││
│ │Shuffler │ │Shuffler │ │Shuffler │    │    Strategy      │ │    Strategy      ││  Grading   │ │  Grading   │ │  Grading   ││
│ ├─────────┤ ├─────────┤ ├─────────┤    ├──────────────────┤ ├──────────────────┤├────────────┤ ├────────────┤ ├────────────┤│
│ │Giữ nguyên│ │Xáo cả   │ │Chỉ xáo  │    │Thi 1 lèo (BJT,   │ │Thi từng Part     ││Chấm số câu │ │Đổi điểm     │ │Chấm tự động││
│ │thứ tự   │ │câu+đáp  │ │đáp án   │    │TOEIC, HSK)       │ │(JLPT, IELTS)     ││đúng chuẩn  │ │bảng quy đổi│ │+ AI/Giáo   ││
│ │gốc      │ │án       │ │A,B,C,D  │    └──────────────────┘ └──────────────────┘└────────────┘ └────────────┘ │viên chấm  ││
│ └─────────┘ └─────────┘ └─────────┘                                                                             └────────────┘│
└──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 📁 3. SƠ ĐỒ 2: CẤU TRÚC THƯ MỤC CÂY PACKAGE / FOLDER / CLASS HOÀN CHỈNH

```text
src/main/java/com/example/exam_system/features/exam_attempt/
│
├── controller/
│   └── ExamAttemptController.java               # REST API Endpoints (/start, /get-question, /auto-save, /submit)
│
├── dto/
│   ├── request/
│   │   ├── AnswerRequest.java                   # DTO gửi câu trả lời thí sinh
│   │   └── AttemptStartRequest.java             # DTO bắt đầu phiên thi
│   └── response/
│       ├── AttemptSubmitResponse.java           # DTO trả về kết quả nộp bài
│       └── GradingResultResponse.java          # DTO điểm số & số câu đúng
│
├── entity/
│   ├── ExamAttempt.java                         # Entity quản lý phiên thi (status, totalScore, currentPart...)
│   └── StudentAnswer.java                      # Entity lưu chi tiết đáp án đã chọn
│
├── repository/
│   ├── ExamAttemptRepository.java
│   └── StudentAnswerRepository.java
│
├── service/
│   └── ExamAttemptService.java                  # Service điều phối chính (Thin Orchestrator & Transaction Boundary)
│
└── pattern/                                     # TẦNG PATTERN (Đa bài thi / Đa ngôn ngữ)
    │
    ├── strategy/                                # 1. CÁC CONTRACT INTERFACE CHÍNH
    │   ├── ExamStrategy.java                    #    [Interface gốc] Cổng giao tiếp chung
    │   │
    │   ├── shuffling/                           #    [TRỤC 1] XÁO TRỘN CÂU HỎI & ĐÁP ÁN
    │   │   ├── ExamShufflerStrategy.java        #    - Interface định nghĩa quy tắc xáo trộn
    │   │   └── impl/
    │   │       ├── NoOpShufflerImpl.java        #    + Giữ nguyên thứ tự gốc (VD: Bài Đọc hiểu)
    │   │       ├── FullShufflerImpl.java        #    + Xáo trộn cả Câu hỏi lẫn Đáp án A,B,C,D
    │   │       └── OptionOnlyShufflerImpl.java  #    + Giữ thứ tự câu hỏi, chỉ xáo trộn đáp án
    │   │
    │   ├── workflow/                            #    [TRỤC 2] QUẢN LÝ LUỒNG THI
    │   │   ├── ExamWorkflowStrategy.java        #    - Interface định nghĩa quy trình thi
    │   │   └── impl/
    │   │       ├── ContinuousWorkflowImpl.java  #    + Thi liên tục 1 lèo (BJT, TOEIC, HSK)
    │   │       └── SectionalWorkflowImpl.java   #    + Thi chia từng Part (JLPT, IELTS)
    │   │
    │   └── grading/                             #    [TRỤC 3] QUẢN LÝ TÍNH ĐIỂM
    │       ├── GradingStrategy.java             #    - Interface định nghĩa thuật toán chấm
    │       └── impl/
    │           ├── RawScoreGradingImpl.java     #    + Chấm số câu đúng đơn giản
    │           ├── ScaledScoreGradingImpl.java  #    + Chấm theo Bảng quy đổi DB (TOEIC, BJT)
    │           ├── PassFailGradingImpl.java     #    + Chấm xét điểm liệt (JLPT)
    │           └── AsyncHybridGradingImpl.java  #    + Chấm tự động + AI/Giáo viên (IELTS, HSK)
    │
    ├── impl/                                    # 2. IMPLEMENTATION CHO CÁC BÀI THI CỤ THỂ
    │   ├── BJTPattern.java                      #    Bài thi BJT (FullShuffler + Continuous + ScaledScore)
    │   ├── JLPTPattern.java                     #    Bài thi JLPT (NoOpShuffler + Sectional + PassFail)
    │   ├── TOEICPattern.java                    #    Bài thi TOEIC (OptionOnly + Continuous + ScaledScore)
    │   ├── IELTSPattern.java                    #    Bài thi IELTS (NoOpShuffler + Sectional + AsyncHybrid)
    │   ├── HSKPattern.java                      #    Bài thi HSK (OptionOnly + Continuous + RawScore)
    │   └── TOPIKPattern.java                    #    Bài thi TOPIK (FullShuffler + Sectional + RawScore)
    │
    └── factory/                                 # 3. QUẢN LÝ KHỞI TẠO (FACTORIES)
        ├── ExamFactory.java                     #    Factory lấy ExamStrategy theo ExamType
        ├── ShufflerFactory.java                 #    Factory lấy ShufflerStrategy
        ├── WorkflowFactory.java                 #    Factory lấy WorkflowStrategy
        └── GradingFactory.java                  #    Factory lấy GradingStrategy
```

---

## ⚙️ 4. CHI TIẾT VÀI TRÒ & LUỒNG HOẠT ĐỘNG CỦA TỪNG TRỤC

### 🔹 Trục 1: Xáo Trộn (`ShufflingStrategy`)
- **Nhiệm vụ:** Quy định cách xáo trộn câu hỏi và các đáp án A, B, C, D khi gọi `getQuestion()`.
- **Lưu ý kiến trúc Seed-Based:** 
  Dùng `new Random(attempt.getId())` để đảm bảo khi thí sinh bấm **F5 (Reload trang)**, thứ tự câu hỏi và đáp án đã xáo trộn **vẫn giữ nguyên không bị đảo lại**, tránh hoang mang cho thí sinh.

### 🔹 Trục 2: Luồng Thi (`WorkflowStrategy`)
- **Nhiệm vụ:** Xử lý logic chuyển đổi trạng thái phiên thi (`IN_PROGRESS`, `SUBMITTED`, `GRADED`).
- **ContinuousWorkflowImpl:** Dùng cho bài thi 1 lèo (BJT, TOEIC, HSK). Nộp bài là khóa toàn bộ thi.
- **SectionalWorkflowImpl:** Dùng cho bài thi chia Part (JLPT, IELTS). Mỗi lần nộp sẽ cập nhật `currentPart`. Chỉ khi hết Part mới nộp toàn bài.

### 🔹 Trục 3: Chấm Điểm (`GradingStrategy`)
- **Nhiệm vụ:** Tính toán điểm số dựa trên đáp án thí sinh và đáp án đúng trong DB.
- **ScaledScoreGradingImpl:** Tra cứu bảng quy đổi điểm (`exam_score_conversions`) trong DB thay vì hardcode trong Java (Ví dụ: 45 câu TOEIC = 230 điểm).
- **PassFailGradingImpl:** Kiểm tra tổng điểm + xét điểm liệt từng phần (JLPT).
- **AsyncHybridGradingImpl:** Chấm trắc nghiệm tự động ngay lập tức, sau đó đẩy phần Tự luận/Nói/Viết vào Queue (RabbitMQ/Kafka/Spring Events) cho AI hoặc Giáo viên chấm sau.

---

## 🛠️ 5. HƯỚNG DẪN KHI BẮT ĐẦU MỞ RỘNG BÀI THI MỚI (CHẲNG HẠN TOEIC/HSK)

Khi cần thêm bài thi mới, thực hiện đúng các bước sau:

1. **Tạo Class Pattern mới** kế thừa `ExamStrategy` (ví dụ `TOEICPattern.java` trong `pattern/impl/`).
2. **Inject các Sub-Strategy phù hợp** vào Constructor:
   ```java
   @Component
   @RequiredArgsConstructor
   public class TOEICPattern implements ExamStrategy {
       private final OptionOnlyShufflerImpl shufflerStrategy;
       private final ContinuousWorkflowImpl workflowStrategy;
       private final ScaledScoreGradingImpl gradingStrategy;

       @Override
       public String examType() {
           return EXAMTYPE.TOEIC.name();
       }
       // Triển khai getQuestion và submitExam thông qua 3 sub-strategy trên
   }
   ```
3. **Không cần sửa bất kỳ dòng code nào** trong `ExamAttemptService`, `BJTPattern` hay `JLPTPattern`.
4. Hệ thống sẽ tự động bắt lấy `TOEICPattern` thông qua `ExamFactory` nhờ cơ chế Dependency Injection của Spring Boot!

---

## 🚀 6. LỘ TRÌNH PHÁT TRIỂN & NÂNG CẤP HỆ THỐNG (ASYNC, THREAD POOL, SSE & AI ANALYSIS)

Tài liệu hướng dẫn lộ trình nâng cấp hệ thống theo hướng **Chấm điểm trắc nghiệm tức thì + Phân tích năng lực bằng AI (Bất đồng bộ - Async & Realtime SSE)**.

```text
Người dùng nộp bài (submitExam)
       │
       ├── [Đồng bộ - Sync] Chấm điểm trắc nghiệm → Trả kết quả điểm ngay lập tức (vài ms)
       │
       └── [Bất đồng bộ - Async] Đẩy tác vụ phân tích cho AI (Thread Pool)
                │
                ├── Gọi API LLM phân tích điểm mạnh/điểm yếu (3-10s)
                ├── Lưu kết quả phân tích & hỗ trợ Chat giải đáp câu sai (Bảng chat_history)
                └── Push kết quả về FE qua SSE (Server-Sent Events) khi phân tích xong
```

---

### 📚 Khối 1: Java Thread & Thread Pool cơ bản (2-3 ngày)
* **Mục tiêu:** Nắm vững khái niệm đa luồng cơ bản để tự tin trả lời phỏng vấn.
* **Kiến thức cốt lõi:**
  - **Thread:** Phân biệt `Main Thread` (xử lý HTTP Request) vs `Worker Thread` (xử lý tác vụ ngầm).
  - **Lý do áp dụng:** Gọi API ngoài (LLM/AI) tốn từ 3-10s, nếu chạy trên Main Thread sẽ gây ngẽn (blocking request), giảm throughput của ứng dụng.
  - **Thread Pool:** Nguyên lý tái sử dụng Thread có sẵn thay vì tạo mới liên tục (tránh cạn kiệt bộ nhớ Server).

---

### ⚙️ Khối 2: Spring Boot `@Async` & `ThreadPoolTaskExecutor` (2-3 ngày)
* **Mục tiêu:** Áp dụng xử lý bất đồng bộ trong Spring Framework.
* **Các bước triển khai:**
  1. Bật cơ chế Async với `@EnableAsync` trong `@Configuration`.
  2. Khởi tạo Bean `ThreadPoolTaskExecutor` với các thông số:
     - `corePoolSize`: Số lượng thread tối thiểu luôn duy trì trong pool.
     - `maxPoolSize`: Số lượng thread tối đa khi queue bị đầy.
     - `queueCapacity`: Hàng chờ chứa các task chưa được xử lý.
  3. Gắn `@Async` tại method xử lý phân tích AI trong Service layer.

---

### 📡 Khối 3: Realtime Notification với SSE (Server-Sent Events) (1-2 ngày)
* **Mục tiêu:** Truyền tải kết quả phân tích AI từ Server về Client theo thời gian thực không cần reload trang.
* **Các bước triển khai:**
  1. **Tạo Endpoint đăng ký lắng nghe SSE:** `GET /api/exam-attempt/{id}/ai-analysis/subscribe` trả về `SseEmitter`.
  2. **Quản lý Emitter:** Lưu trữ kết quả `SseEmitter` theo `attemptId` trong một Map/Registry.
  3. **Push Event:** Khi worker thread hoàn thành phân tích AI ➔ Gọi `emitter.send(aiResult)` ➔ Đóng kết nối `emitter.complete()`.

---

### 🤖 Khối 4: AI Analysis & Chat History Integration (Thiết kế & Tích hợp)
* **Mục tiêu:** Xây dựng tính năng phân tích năng lực và trợ lý AI giải thích câu sai.
* **Thiết kế Database:**
  - Bảng `ai_analysis_result`: Lưu điểm mạnh, điểm yếu, lộ trình cải thiện của từng `exam_attempt`.
  - Bảng `chat_history`: Lưu hội thoại giữa người dùng và Trợ lý AI khi trao đổi về các câu hỏi trong bài thi.
* **Luồng tích hợp LLM:**
  - Tổng hợp dữ liệu câu hỏi, đáp án đúng, đáp án học sinh chọn ➔ Gửi Prompt cho LLM API (OpenAI/Gemini).
  - Nhận JSON response ➔ Lưu DB ➔ Báo về cho FE qua SSE.

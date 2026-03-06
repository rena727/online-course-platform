// quiz.js faylının tam tərkibi

const questions = {
    csharp: [
        { q: "C# dilində tam ədədləri saxlamaq üçün hansı tip istifadə olunur?", options: ["string", "int", "bool", "double"], correct: 1 },
        { q: ".NET hansı şirkət tərəfindən yaradılıb?", options: ["Apple", "Google", "Microsoft", "Oracle"], correct: 2 },
        { q: "Sabit dəyişən yaratmaq üçün hansı söz istifadə olunur?", options: ["static", "const", "fixed", "void"], correct: 1 }
    ],
    sql: [
        { q: "Məlumatları oxumaq üçün hansı əmr istifadə olunur?", options: ["UPDATE", "INSERT", "DELETE", "SELECT"], correct: 3 },
        { q: "Cədvələ yeni sətir əlavə etmək üçün əmr?", options: ["ADD", "INSERT INTO", "CREATE", "UPDATE"], correct: 1 }
    ],
    aspnet: [
        { q: "Middleware nə üçün istifadə olunur?", options: ["Database üçün", "Request/Response idarəsi", "CSS üçün", "Səhifə silmək"], correct: 1 },
        { q: "Dependency Injection-ın məqsədi?", options: ["Sürəti azaltmaq", "Zəif bağlılıq (Loose coupling)", "Yalnız DB", "Dizayn"], correct: 1 }
    ]
};

let currentQuestions = [];
let currentQuestionIndex = 0;
let userAnswers = [];
let timeLeft = 600; 
let timerInterval;

function startQuiz(type) {
    console.log("İmtahan başlayır: " + type); // Konsolda yoxlamaq üçün
    
    if (!questions[type]) {
        alert("Sual tapılmadı!");
        return;
    }
    
    currentQuestions = questions[type];
    currentQuestionIndex = 0;
    userAnswers = new Array(currentQuestions.length).fill(null);
    timeLeft = 600;

    // Elementləri tapırıq
    const lobby = document.getElementById('exam-lobby');
    const container = document.getElementById('quiz-container');
    const title = document.getElementById('quiz-title');

    if (lobby && container) {
        lobby.classList.add('d-none');
        container.classList.remove('d-none');
        if (title) title.innerText = type.toUpperCase() + " İmtahanı";
        
        showQuestion();
        startTimer();
    } else {
        console.error("HTML elementləri tapılmadı!");
    }
}

function showQuestion() {
    const question = currentQuestions[currentQuestionIndex];
    document.getElementById('q-counter').innerText = `Sual ${currentQuestionIndex + 1} / ${currentQuestions.length}`;
    document.getElementById('question-text').innerText = question.q;
    
    const container = document.getElementById('options-container');
    container.innerHTML = '';
    
    question.options.forEach((opt, index) => {
        const div = document.createElement('div');
        div.className = `option-item ${userAnswers[currentQuestionIndex] === index ? 'selected' : ''}`;
        div.innerText = opt;
        div.onclick = () => {
            userAnswers[currentQuestionIndex] = index;
            showQuestion();
        };
        container.appendChild(div);
    });

    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');

    if (prevBtn) prevBtn.classList.toggle('d-none', currentQuestionIndex === 0);
    if (nextBtn) nextBtn.innerText = (currentQuestionIndex === currentQuestions.length - 1) ? "Bitir" : "Növbəti";
    
    const progress = ((currentQuestionIndex + 1) / currentQuestions.length) * 100;
    document.getElementById('quiz-progress').style.width = progress + "%";
}

// Düymə kliklərini JS-də təyin edirik (HTML-də səhv olmasın deyə)
document.addEventListener('DOMContentLoaded', () => {
    const nextBtn = document.getElementById('next-btn');
    const prevBtn = document.getElementById('prev-btn');

    if (nextBtn) {
        nextBtn.onclick = () => {
            if (userAnswers[currentQuestionIndex] === null) {
                alert("Zəhmət olmasa bir variant seçin!");
                return;
            }
            if (currentQuestionIndex < currentQuestions.length - 1) {
                currentQuestionIndex++;
                showQuestion();
            } else {
                finishQuiz();
            }
        };
    }

    if (prevBtn) {
        prevBtn.onclick = () => {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                showQuestion();
            }
        };
    }
});

function startTimer() {
    clearInterval(timerInterval);
    timerInterval = setInterval(() => {
        timeLeft--;
        let mins = Math.floor(timeLeft / 60);
        let secs = timeLeft % 60;
        const timerEl = document.getElementById('timer');
        if (timerEl) {
            timerEl.innerHTML = `<i class="far fa-clock me-2"></i>${mins}:${secs < 10 ? '0'+secs : secs}`;
        }
        if (timeLeft <= 0) finishQuiz();
    }, 1000);
}

function finishQuiz() {
    clearInterval(timerInterval);
    let correctCount = 0;
    currentQuestions.forEach((q, i) => {
        if (userAnswers[i] === q.correct) correctCount++;
    });

    const score = Math.round((correctCount / currentQuestions.length) * 100);
    document.getElementById('quiz-container').classList.add('d-none');
    document.getElementById('result-screen').classList.remove('d-none');
    document.getElementById('score-text').innerText = score + "%";
    document.getElementById('result-message').innerText = score >= 50 ? "Təbriklər, keçdiniz!" : "Kəsildiniz.";
}


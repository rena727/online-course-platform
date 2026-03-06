// Kurs Məlumatları (Gələcəkdə bura API-dan gələcək)
const courseData = {
    sql: {
        name: "SQL Master dərsləri",
        lessons: [
            { title: "1. Verilənlər bazası nədir?", url: "https://www.youtube.com/embed/HXV3zeQKqGY" },
            { title: "2. SELECT sorğuları", url: "https://www.youtube.com/embed/7S_tz1z_5bA" },
            { title: "3. JOIN əməliyyatları", url: "https://www.youtube.com/embed/9yeOJ0ZMUYw" }
        ]
    },
    csharp: {
        name: "C# Giriş Kursu",
        lessons: [
            { title: "1. C# və .NET Giriş", url: "https://www.youtube.com/embed/GhQdlIFylQ8" },
            { title: "2. Dəyişənlər və Tiplər", url: "https://www.youtube.com/embed/rav0uOnV-q4" }
        ]
    }
};

let currentCourse = null;

// Kursa Giriş Yoxlaması
function accessCourse(id, isPaid, price) {
    currentCourse = courseData[id];
    if (isPaid) {
        document.getElementById('pay-price').innerText = price;
        document.getElementById('payment-modal').classList.remove('d-none');
    } else {
        openVideoPanel();
    }
}

// Ödəniş Təsdiqi
function confirmPayment() {
    alert("Ödəniş uğurludur! Kurs açıldı.");
    document.getElementById('payment-modal').classList.add('d-none');
    openVideoPanel();
}

function closePayment() {
    document.getElementById('payment-modal').classList.add('d-none');
}

// Video Paneli Açmaq
function openVideoPanel() {
    const videoPanel = document.getElementById('video-panel');
    if(videoPanel) {
        videoPanel.style.display = 'block';
        document.getElementById('active-course-name').innerText = currentCourse.name;

        const listBox = document.getElementById('lesson-list-box');
        listBox.innerHTML = '';

        currentCourse.lessons.forEach((lesson, index) => {
            const div = document.createElement('div');
            div.className = 'list-group-item list-group-item-action lesson-item';
            div.innerHTML = `<i class="fas fa-play-circle me-2 text-primary"></i> ${lesson.title}`;
            div.onclick = () => playLesson(lesson.url, lesson.title, div);
            listBox.appendChild(div);
        });

        // İlk dərsi başlat
        playLesson(currentCourse.lessons[0].url, currentCourse.lessons[0].title, listBox.firstChild);
    }
}

function playLesson(url, title, el) {
    document.getElementById('mainVideo').src = url;
    document.getElementById('video-title').innerText = title;

    document.querySelectorAll('.lesson-item').forEach(i => i.classList.remove('active'));
    el.classList.add('active');
}

function closeVideoPanel() {
    document.getElementById('video-panel').style.display = 'none';
    document.getElementById('mainVideo').src = '';
}

// Axtarış Sistemi
function searchCourse() {
    let input = document.getElementById('courseSearch').value.toLowerCase();
    document.querySelectorAll('.course-item').forEach(item => {
        let title = item.querySelector('.c-title').innerText.toLowerCase();
        item.style.display = title.includes(input) ? "block" : "none";
    });
}

// Kateqoriya Filtri
function filterCat(cat) {
    document.querySelectorAll('.course-item').forEach(item => {
        const type = item.getAttribute('data-type');
        if (cat === 'all' || type.includes(cat)) {
            item.style.display = "block";
        } else {
            item.style.display = "none";
        }
    });
}
document.addEventListener('DOMContentLoaded', () => {
    const chatCircle = document.getElementById('chat-circle');
    const chatBox = document.getElementById('chat-box');
    const chatClose = document.getElementById('chat-close');
    const chatInput = document.getElementById('chat-input');
    const chatSend = document.getElementById('chat-send');
    const chatMessages = document.getElementById('chat-messages');

    if (chatCircle) {
        chatCircle.onclick = () => chatBox.classList.toggle('d-none');
        chatClose.onclick = () => chatBox.classList.add('d-none');

        const sendMessage = () => {
            const text = chatInput.value.trim();
            if (text === "") return;

            // User message
            const userMsg = `<div class="text-end mb-3"><div class="p-2 px-3 rounded-3 bg-primary text-white shadow-sm d-inline-block small text-start">${text}</div></div>`;
            chatMessages.innerHTML += userMsg;
            chatInput.value = "";
            chatMessages.scrollTop = chatMessages.scrollHeight;

            // Bot response logic
            setTimeout(() => {
                let botReply = "Sualınızı qeydə aldıq. Mentorlarımız tezliklə cavab verəcək!";
                if(text.toLowerCase().includes("salam")) botReply = "Salam! EduMaster-ə xoş gəlmisiniz. Sizə necə kömək edə bilərəm?";
                
                const botMsg = `<div class="bot-msg mb-3"><div class="p-2 px-3 rounded-3 bg-white shadow-sm d-inline-block small border">${botReply}</div></div>`;
                chatMessages.innerHTML += botMsg;
                chatMessages.scrollTop = chatMessages.scrollHeight;
            }, 1000);
        };

        chatSend.onclick = sendMessage;
        chatInput.onkeypress = (e) => { if(e.key === "Enter") sendMessage(); };
    }
});

document.addEventListener('DOMContentLoaded', () => {
    
    // --- 1. ADMIN PANEL TAB KEÇİDLƏRİ ---
    window.showTab = (id, el) => {
        // Bütün bölmələri gizlət
        const sections = document.querySelectorAll('.content-section');
        sections.forEach(s => s.classList.remove('active'));

        // Seçilən bölməni göstər
        const target = document.getElementById(id);
        if (target) {
            target.classList.add('active');
        }

        // Sidebar-da aktiv klassı dəyiş
        const navLinks = document.querySelectorAll('.nav-link');
        navLinks.forEach(l => l.classList.remove('active'));
        if (el) {
            el.classList.add('active');
        }
    };

    // --- 2. STATİSTİKA QRAFİKİ (Chart.js) ---
    const ctx = document.getElementById('activityChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['1 Jan', '3 Jan', '5 Jan', '7 Jan', '9 Jan', '10 Jan'],
                datasets: [{
                    label: 'Video Baxış Sayı',
                    data: [1200, 1900, 1500, 2500, 2200, 3100],
                    borderColor: '#3f83f8',
                    backgroundColor: 'rgba(63, 131, 248, 0.1)',
                    fill: true,
                    tension: 0.4
                }]
            },
            options: {
                responsive: true,
                plugins: { legend: { display: false } },
                scales: {
                    y: { beginAtZero: true, grid: { display: false } },
                    x: { grid: { display: false } }
                }
            }
        });
    }

    // --- 3. MODERASİYA ƏMƏLİYYATLARI (Təsdiq/Rədd) ---
    window.approveAction = (name, type) => {
        const confirmMsg = `${name} adlı ${type === 'video' ? 'videonu' : 'müəllimi'} təsdiqləyirsiniz?`;
        if (confirm(confirmMsg)) {
            alert("Uğurla təsdiqləndi! Sistem yeniləndi.");
            // Burada gələcəkdə backend-ə sorğu gedəcək
        }
    };

    window.blockUser = (userName) => {
        if (confirm(`${userName} adlı istifadəçini bloklamaq istədiyinizə əminsiniz?`)) {
            alert("İstifadəçi bloklandı. Artıq sistemə giriş edə bilməyəcək.");
        }
    };

    // --- 4. CHATBOT (Əvvəlki kodun davamı) ---
    const chatCircle = document.getElementById('chat-circle');
    const chatBox = document.getElementById('chat-box');
    if (chatCircle) {
        chatCircle.onclick = () => chatBox.classList.toggle('d-none');
    }
});

// Şikayət məlumatları (Normalda bazadan gələcək)
const reportsData = {
    1: {
        title: "Python Giriş videosunda problem",
        type: "Uyğunsuz Məzmun",
        time: "12 dəq əvvəl",
        reporter: "Rəsul M.",
        target: "Video #502 (Python Kursu)",
        reason: "Videonun 05:40 dəqiqəsində müəllim qeyri-etik ifadə işlədir və mövzudan kənar danışır."
    },
    2: {
        title: "Elvin T. təhqir edir",
        type: "Təhqir / Spam",
        time: "1 saat əvvəl",
        reporter: "Leyla Q.",
        target: "User: Elvin Teymurov",
        reason: "Bu istifadəçi rəy bölməsində digər tələbələri aşağılayır və spam linklər paylaşır."
    }
};

window.viewReport = (id, el) => {
    const data = reportsData[id];
    if (!data) return;

    // UI-ni dəyiş
    document.getElementById('empty-state').classList.add('d-none');
    const detail = document.getElementById('report-detail');
    detail.classList.remove('d-none');

    // Məlumatları doldur
    document.getElementById('detTitle').innerText = data.title;
    document.getElementById('detType').innerText = data.type;
    document.getElementById('detTime').innerText = "Zaman: " + data.time;
    document.getElementById('detReporterName').innerText = data.reporter;
    document.getElementById('detTargetName').innerText = data.target;
    document.getElementById('detReason').innerText = data.reason;

    // Aktivlik klassını yenilə
    document.querySelectorAll('.report-item').forEach(i => i.classList.remove('active'));
    el.classList.add('active');
};

window.confirmBan = () => {
    bootstrap.Modal.getInstance(document.getElementById('banModal')).hide();
    alert("İstifadəçi sistemdən uzaqlaşdırıldı!");
    location.reload(); // Reallıqda sadəcə siyahıdan silinəcək
};

// profilee
 function showTab(id, el) {
        // Bütün bölmələri gizlət
        document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
        // Seçilən bölməni göstər
        document.getElementById(id).classList.add('active');
        
        // Menyu rənglərini dəyiş
        document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
        el.classList.add('active');
    }

    // moderationn


    let currentId = null;
    const player = document.getElementById('mainPlayer');
    const toast = new bootstrap.Toast(document.getElementById('liveToast'));

    function loadVideo(id, title, instructor, url, el) {
        currentId = id;
        document.getElementById('vTitle').innerText = title;
        document.getElementById('vInfo').innerText = "Müəllim: " + instructor;
        player.src = url;
        player.play();

        // Düymələri aktiv et
        document.getElementById('btnReject').disabled = false;
        document.getElementById('btnApprove').disabled = false;

        document.querySelectorAll('.video-item').forEach(i => i.classList.remove('active'));
        el.classList.add('active');
    }

    function finalApprove() {
        bootstrap.Modal.getInstance(document.getElementById('approveModal')).hide();
        toast.show();
        // Buradan backend-ə ID göndəriləcək
    }

    function finalReject() {
        const text = document.getElementById('rejectText').value;
        if(text.length < 5) return;
        bootstrap.Modal.getInstance(document.getElementById('rejectModal')).hide();
        toast.show();
        // Buradan backend-ə ID və səbəb göndəriləcək
    }

    // mentors 

    const mentorlar = {
            ali: {
                name: "Əli Məmmədov", role: "Senior Backend Developer", img: "https://randomuser.me/api/portraits/men/1.jpg",
                exp: "10+ il", std: "1.2k", rate: "4.9",
                courses: [{title: "SQL Master dərsləri", url: "courses.html"}, {title: "C# Fundamentals", url: "courses.html"}],
                comments: ["'Mükəmməl izah tərzi var.' - Toğrul", "'Hər şeyi praktiki göstərir.' - Səbinə"]
            },
            leyla: {
                name: "Leyla Quliyeva", role: "Cloud Architect", img: "https://randomuser.me/api/portraits/women/2.jpg",
                exp: "8 il", std: "900+", rate: "5.0",
                courses: [{title: "Cloud Computing 101", url: "courses.html"}],
                comments: ["'Azure dərsləri möhtəşəmdir.' - Murad"]
            }
        };

        function showMentor(id) {
            const m = mentorlar[id];
            document.getElementById('m-name').innerText = m.name;
            document.getElementById('m-role').innerText = m.role;
            document.getElementById('m-img').src = m.img;
            document.getElementById('m-exp').innerText = m.exp;
            document.getElementById('m-std').innerText = m.std;
            document.getElementById('m-rate').innerText = m.rate;

            const cDiv = document.getElementById('m-courses');
            cDiv.innerHTML = m.courses.map(c => `
                <a href="${c.url}" class="course-link">
                    <span><i class="fas fa-play-circle me-2 text-primary"></i> ${c.title}</span>
                    <i class="fas fa-chevron-right small opacity-50"></i>
                </a>
            `).join('');

            const rDiv = document.getElementById('m-comments');
            rDiv.innerHTML = m.comments.map(r => `<div class="bg-light p-2 rounded mb-2">${r}</div>`).join('');

            new bootstrap.Modal(document.getElementById('mentorModal')).show();
        }

       // --- 1. KURS İDARƏETMƏSİ (Sənin strukturuna uyğun əlavə) ---
       function accessCourse(id, isPaid, price) {
           // Kurs məlumatlarını tapırıq (courseData yuxarıda olmalıdır)
           currentCourse = courseData[id];
           if (isPaid) {
               document.getElementById('pay-price').innerText = price;
               document.getElementById('payment-modal').classList.remove('d-none');
           } else {
               openVideoPanel();
           }
       }

       function openVideoPanel() {
           // Sənin istədiyin əsas dəyişiklik: Katalogu gizlət, Paneli göstər
           const catalog = document.getElementById('catalog-section');
           const player = document.getElementById('video-panel');

           if(catalog) catalog.style.display = 'none';
           if(player) player.style.display = 'block';

           document.getElementById('active-course-name').innerText = currentCourse.name;

           const listBox = document.getElementById('lesson-list-box');
           listBox.innerHTML = '';

           currentCourse.lessons.forEach((lesson, index) => {
               listBox.innerHTML += `
                   <div class="lesson-item" onclick="playLesson('${lesson.url}', '${lesson.title}', this)">
                       <i class="fas fa-play-circle me-2 text-primary"></i> ${lesson.title}
                   </div>
               `;
           });

           playLesson(currentCourse.lessons[0].url, currentCourse.lessons[0].title, document.querySelector('.lesson-item'));
       }

       function closeVideoPanel() {
           // Paneli gizlət, Katalogu qaytar
           document.getElementById('video-panel').style.display = 'none';
           document.getElementById('catalog-section').style.display = 'block';
           document.getElementById('mainVideo').src = '';
       }

       // --- 2. CHATBOT MƏNTİQİ ---
       document.addEventListener('DOMContentLoaded', () => {
           const chatCircle = document.getElementById('chat-circle');
           const chatBox = document.getElementById('chat-box');
           const chatInput = document.getElementById('chat-input');
           const chatSend = document.getElementById('chat-send');
           const chatMessages = document.getElementById('chat-messages');

           if (chatCircle) {
               chatCircle.onclick = () => chatBox.classList.toggle('d-none');

               const sendMessage = () => {
                   const text = chatInput.value.trim();
                   if (text === "") return;

                   const userMsg = `<div class="text-end mb-3"><div class="p-2 px-3 rounded-3 bg-primary text-white shadow-sm d-inline-block small text-start">${text}</div></div>`;
                   chatMessages.innerHTML += userMsg;
                   chatInput.value = "";
                   chatMessages.scrollTop = chatMessages.scrollHeight;

                   setTimeout(() => {
                       let botReply = "Sualınızı qeydə aldıq. Mentorlarımız tezliklə cavab verəcək!";
                       if(text.toLowerCase().includes("salam")) botReply = "Salam! EduMaster-ə xoş gəlmisiniz.";

                       const botMsg = `<div class="bot-msg mb-3"><div class="p-2 px-3 rounded-3 bg-white shadow-sm d-inline-block small border">${botReply}</div></div>`;
                       chatMessages.innerHTML += botMsg;
                       chatMessages.scrollTop = chatMessages.scrollHeight;
                   }, 1000);
               };

               if(chatSend) chatSend.onclick = sendMessage;
               if(chatInput) chatInput.onkeypress = (e) => { if(e.key === "Enter") sendMessage(); };
           }
       });

       // --- 3. ADMIN PANEL VƏ STATİSTİKA (Chart.js) ---
       window.showTab = (id, el) => {
           document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
           const target = document.getElementById(id);
           if (target) target.classList.add('active');

           document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
           if (el) el.classList.add('active');
       };

       // --- 4. MODERASİYA VƏ ŞİKAYƏTLƏR ---
       window.viewReport = (id, el) => {
           const data = reportsData[id];
           if (!data) return;
           document.getElementById('empty-state').classList.add('d-none');
           document.getElementById('report-detail').classList.remove('d-none');
           document.getElementById('detTitle').innerText = data.title;
           document.getElementById('detType').innerText = data.type;
           document.getElementById('detReason').innerText = data.reason;
           document.querySelectorAll('.report-item').forEach(i => i.classList.remove('active'));
           el.classList.add('active');
       };

       // --- 5. MENTORLAR MƏLUMATI ---
       function showMentor(id) {
           const m = mentorlar[id];
           document.getElementById('m-name').innerText = m.name;
           document.getElementById('m-role').innerText = m.role;
           document.getElementById('m-img').src = m.img;

           const cDiv = document.getElementById('m-courses');
           cDiv.innerHTML = m.courses.map(c => `
               <a href="${c.url}" class="course-link">
                   <span><i class="fas fa-play-circle me-2 text-primary"></i> ${c.title}</span>
               </a>
           `).join('');

           new bootstrap.Modal(document.getElementById('mentorModal')).show();
       }

       // Digər köməkçi funksiyalar (playLesson, filterCat, searchCourse və s.) eynilə qalır...



       function showTab(tabId, element) {
           // 1. Bütün bölmələri (Dashboard, Kurslarım, İmtahanlar və s.) gizlət
           const sections = document.querySelectorAll('.content-section');
           sections.forEach(section => {
               section.style.display = 'none';
               section.classList.remove('active');
           });

           // 2. Kliklənən düyməyə uyğun olan bölməni tap və göstər
           const activeSection = document.getElementById(tabId);
           if (activeSection) {
               activeSection.style.display = 'block';
               activeSection.classList.add('active');
           }

           // 3. Sidebar-dakı bütün düymələrdən 'active' (göy rəng) klasını sil
           const navLinks = document.querySelectorAll('.nav-link');
           navLinks.forEach(link => {
               link.classList.remove('active');
           });

           // 4. Yalnız kliklənən düyməni aktiv (göy rəng) et
           element.classList.add('active');
       }

       // Səhifə ilk dəfə açılanda avtomatik Dashboard bölməsini göstərsin
       document.addEventListener("DOMContentLoaded", function() {
           const dashboardLink = document.querySelector('.nav-link.active');
           if (dashboardLink) {
               showTab('dashboard', dashboardLink);
           }
       });



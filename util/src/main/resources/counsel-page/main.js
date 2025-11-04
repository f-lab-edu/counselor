// main.js

// CSS Helper Styles (화면 분리 시 각 파일에 포함하지 않고 JS에서 동적으로 관리)
const customStyles = `
    .bg-primary { background-color: #0d1117; } /* 다크 네이비 */
    .text-primary { color: #0d1117; }
    .bg-gray-100 { background-color: #f7f7f7; }
    .tab-active { border-bottom: 3px solid #0d1117; font-weight: 600; }
    .tab-button.active { background-color: #0d1117; color: white; }
    .status-tag { padding: 2px 8px; border-radius: 4px; font-size: 0.8rem; font-weight: 600; }
    .status-waiting { background-color: #fff3c9; color: #b45309; }
    .status-progress { background-color: #c7f2c7; color: #166534; }
    .status-done { background-color: #d1e7ff; color: #1e40af; }
    .chat-bubble { max-width: 60%; padding: 10px 15px; border-radius: 18px; margin-bottom: 10px; }
    .chat-bubble.mine { background-color: #0d1117; color: white; margin-left: auto; border-bottom-right-radius: 4px; }
    .chat-bubble.other { background-color: #f0f0f0; color: #333; margin-right: auto; border-bottom-left-radius: 4px; }
`;
$('head').append(`<style>${customStyles}</style>`);


// 화면 경로 맵
const viewMap = {
    customer: [
        { name: '상담 신청하기', file: 'apply.html', id: 'apply' },
        { name: '내 상담 리스트', file: 'list.html', id: 'list' },
        { name: '상담 채팅', file: 'chat.html', id: 'chat' }
    ],
    counselor: [
        { name: '대시보드', file: 'counselor_dashboard.html', id: 'dashboard' },
        { name: '상담 리스트', file: 'counselor_list.html', id: 'counselor-list' }
    ]
};

let currentRole = 'customer';
let currentFile = 'apply.html';

/**
 * 특정 화면 파일을 로드하고 네비게이션을 업데이트합니다.
 * @param {string} fileToLoad - 로드할 HTML 파일 경로 (예: 'apply.html')
 * @param {string} tabId - 활성화할 탭의 ID (예: 'apply')
 */
function loadView(fileToLoad, tabId) {
    currentFile = fileToLoad;
    $('#content-area').load(fileToLoad, function() {
        // 로드 성공 후 탭 활성화 상태 업데이트
        $('#main-nav .tab-button').removeClass('active bg-primary text-white hover:bg-gray-100').addClass('hover:bg-gray-100');
        $(`#main-nav button[data-id="${tabId}"]`).removeClass('hover:bg-gray-100').addClass('active bg-primary text-white');

        // 로드된 화면에 따라 특정 로직 바인딩 함수 호출
        bindViewLogic(tabId);
    });
}

/**
 * 역할(고객/상담사)에 따라 네비게이션 탭을 갱신합니다.
 * @param {string} role - 'customer' 또는 'counselor'
 */
function updateNavigation(role) {
    currentRole = role;
    const tabs = viewMap[role];
    let navHtml = '';

    tabs.forEach(tab => {
        navHtml += `<button class="tab-button px-4 py-2 mr-2 text-sm hover:bg-gray-100" data-file="${tab.file}" data-id="${tab.id}">${tab.name}</button>`;
    });

    $('#main-nav').html(navHtml);

    // 네비게이션 버튼에 클릭 이벤트 바인딩
    $('#main-nav').off('click', '.tab-button').on('click', '.tab-button', function() {
        const file = $(this).data('file');
        const id = $(this).data('id');
        loadView(file, id);
    });

    // 기본 화면 로드 (항상 첫 번째 탭)
    loadView(tabs[0].file, tabs[0].id);
}

/**
 * 로드된 화면에 따라 필요한 이벤트 리스너와 로직을 바인딩합니다.
 * AJAX 주석은 여기에 위치합니다.
 */
function bindViewLogic(tabId) {

    // --- 고객 화면 로직 ---
    if (tabId === 'apply') {
        $('#consultation-form').on('submit', function(e) {
            e.preventDefault();

            // **AJAX 주석: 상담 신청 데이터 서버로 전송**
            // $.ajax({
            //     url: '/api/consultation/apply', type: 'POST', data: $(this).serialize(),
            //     success: function(response) {
            //         alert('상담 신청이 완료되었습니다.');
            //         loadView('list.html', 'list'); // 리스트 화면으로 이동
            //     },
            //     error: function() { alert('신청 오류 발생.'); }
            // });
            alert('상담 신청 완료되었습니다. (예시)');
            loadView('list.html', 'list'); // 리스트 화면으로 이동
        });
    }

    else if (tabId === 'list') {
        // 필터링 로직
        $('#list button[data-list-filter]').on('click', function() {
            $('#list button[data-list-filter]').removeClass('active bg-primary text-white').addClass('hover:bg-gray-100');
            $(this).removeClass('hover:bg-gray-100').addClass('active bg-primary text-white');

            const filter = $(this).data('list-filter');
            $('.consultation-item').each(function() {
                if (filter === 'all' || $(this).data('status') === filter) {
                    $(this).removeClass('hidden');
                } else {
                    $(this).addClass('hidden');
                }
            });

            // **AJAX 주석: 필터링된 상담 리스트 데이터 로드**
            // $.ajax({
            //     url: `/api/customer/consultations?status=${filter}`, type: 'GET',
            //     success: function(data) { /* 리스트 업데이트 로직 */ }
            // });
        });

        // 리스트 항목 클릭 시 채팅 화면으로 이동
        $('#consultation-list').on('click', '.consultation-item', function() {
            loadView('chat.html', 'chat');
            // **AJAX 주석: 해당 상담 ID의 채팅 내역 로드**
            // const consultationId = $(this).find('.font-bold').text();
            // $.ajax({ url: `/api/chat/history/${consultationId}`, type: 'GET' });
        });
    }

    else if (tabId === 'chat') {
        // 채팅 전송 로직
        $('#send-chat-btn').on('click', function() { sendMessage('chat-input', '#chat-messages'); });
        $('#chat-input').on('keypress', function(e) {
            if (e.which == 13) { sendMessage('chat-input', '#chat-messages'); }
        });
    }

    // --- 상담사 화면 로직 ---
    else if (tabId === 'dashboard') {
        // **AJAX 주석: 대시보드 통계 데이터 로드**
        // $.ajax({
        //     url: '/api/counselor/dashboard', type: 'GET',
        //     success: function(data) {
        //         // data를 이용하여 차트 그리기
        //         drawWeeklyChart(data.weeklyData);
        //         drawHourlyChart(data.hourlyData);
        //         drawCategoryChart(data.categoryData);
        //     }
        // });

        // 예시 데이터로 차트 그리기
        drawWeeklyChart();
        drawHourlyChart();
        drawCategoryChart();
    }

    else if (tabId === 'counselor-list') {
        // 필터링 로직 (상담사 리스트)
        $('#counselor-list button[data-counselor-filter]').on('click', function() {
            $('#counselor-list button[data-counselor-filter]').removeClass('active bg-primary text-white').addClass('hover:bg-gray-100');
            $(this).removeClass('hover:bg-gray-100').addClass('active bg-primary text-white');

            const filter = $(this).data('counselor-filter');
            $('#counselor-consultation-list .consultation-item').each(function() {
                if (filter === 'all' || $(this).data('status') === filter) {
                    $(this).removeClass('hidden');
                } else {
                    $(this).addClass('hidden');
                }
            });
            // **AJAX 주석: 상담사 리스트 필터링 데이터 로드**
        });

        // 리스트 항목 클릭 시 상세 채팅 화면으로 이동
        $('#counselor-consultation-list').on('click', '.consultation-item', function() {
            // counselor_chat_detail.html을 메인 영역에 직접 로드
            $('#content-area').load('counselor_chat_detail.html', function() {
                // 상세 화면 로드 후 로직 바인딩
                bindViewLogic('counselor-chat-detail');
            });
        });
    }

    else if (tabId === 'counselor-chat-detail') {
        // 리스트로 돌아가기
        $('#back-to-list').on('click', function() {
            // 메인 탭으로 돌아가기 (상담사 리스트)
            loadView('counselor_list.html', 'counselor-list');
        });

        // 채팅 전송 로직 (상담사 상세)
        $('#counselor-chat-detail-input-btn').on('click', function() { sendMessage('counselor-chat-detail-input-field', '#counselor-chat-messages'); });
        $('#counselor-chat-detail-input-field').on('keypress', function(e) {
            if (e.which == 13) { sendMessage('counselor-chat-detail-input-field', '#counselor-chat-messages'); }
        });

        // **AJAX 주석: 상담 메모 작성, 상담 종료 등 추가 기능 구현 필요**
    }
}

// --- Chart.js 구현 함수 ---

// 주간 상담 트렌드 차트 (막대 그래프)
function drawWeeklyChart() {
    const ctx = document.getElementById('weeklyChart');
    if (!ctx) return;

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['월', '화', '수', '목', '금', '토', '일'],
            datasets: [
                {
                    label: '완료',
                    data: [40, 50, 55, 60, 45, 30, 20],
                    backgroundColor: '#3B82F6', // blue-500
                    barPercentage: 0.6,
                    categoryPercentage: 0.7
                },
                {
                    label: '접수',
                    data: [45, 55, 60, 65, 50, 35, 25],
                    backgroundColor: '#10B981', // green-500
                    barPercentage: 0.6,
                    categoryPercentage: 0.7
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: { stacked: false, grid: { display: false } },
                y: { beginAtZero: true, max: 80, ticks: { stepSize: 20 } }
            }
        }
    });
}

// 시간대별 상담 건수 차트 (라인 그래프)
function drawHourlyChart() {
    const ctx = document.getElementById('hourlyChart');
    if (!ctx) return;

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00'],
            datasets: [{
                label: '상담 건수',
                data: [4, 10, 12, 6, 4, 8, 14, 10, 6],
                borderColor: '#8B5CF6', // purple-500
                backgroundColor: 'rgba(139, 92, 246, 0.2)',
                fill: true,
                tension: 0.4,
                pointBackgroundColor: '#8B5CF6',
                pointBorderColor: '#fff',
                pointHoverBackgroundColor: '#fff',
                pointHoverBorderColor: '#8B5CF6'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: { grid: { display: false } },
                y: { beginAtZero: true, max: 16, ticks: { stepSize: 4 } }
            }
        }
    });
}

// 카테고리별 상담 분포 차트 (도넛 차트)
function drawCategoryChart() {
    const ctx = document.getElementById('categoryChart');
    if (!ctx) return;

    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['제품 문의', '기술 지원', '결제/환불', '일반 상담', '불만 접수'],
            datasets: [{
                data: [37, 25, 17, 14, 6], // 퍼센티지 값
                backgroundColor: [
                    '#3B82F6', // blue-500
                    '#10B981', // green-500
                    '#F59E0B', // orange-500
                    '#8B5CF6', // purple-500
                    '#EF4444'  // red-500
                ],
                hoverOffset: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            }
        }
    });
}


/**
 * 채팅 메시지를 화면에 추가하고 서버로 전송합니다.
 */
function sendMessage(inputId, containerId) {
    const input = $(`#${inputId}`);
    const message = input.val().trim();
    const messagesContainer = $(containerId);

    if (message) {
        const now = new Date();
        const time = now.getHours() + ':' + ('0' + now.getMinutes()).slice(-2);

        const newMessage = `
            <div class="flex flex-col items-end">
                <div class="chat-bubble mine">${message}</div>
                <div class="text-xs text-gray-500">오전 ${time} (예시)</div>
            </div>
        `;
        messagesContainer.append(newMessage);
        input.val('');
        messagesContainer.scrollTop(messagesContainer[0].scrollHeight);

        // **AJAX 주석: 채팅 메시지 서버로 전송**
         $.ajax({
             url: 'http://localhost:8080/chat', type: 'POST',
             contentType: 'application/json',
                  // 2. 데이터는 JSON 문자열로 변환
             data: JSON.stringify({
                 counselId: "counsel10",
                 senderId: "user2",
                 senderType: "U",
                 msg: message
                 })
         });
    }
}


// --- 초기화 및 이벤트 리스너 ---
$(document).ready(function() {
    // 헤더 역할 전환 이벤트
    $('.header-link').on('click', function() {
        const role = $(this).data('view');
        updateNavigation(role);
    });

    // 초기 고객 화면 로드
    updateNavigation('customer');
});
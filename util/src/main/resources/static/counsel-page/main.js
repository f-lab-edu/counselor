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
   .chat-bubble {
           /* ★★★ max-width 재도입 (메시지가 너무 커지는 것 방지) ★★★ */
           max-width: 70%;
           display: block;

           padding: 10px 15px;
           border-radius: 18px;
           margin-bottom: 2px; /* 메시지 간 공백 축소 */

           word-break: keep-all;
           word-wrap: normal;
       }
       .chat-bubble.mine {
           background-color: #0d1117;
           color: white;
           margin-left: auto; /* 오른쪽 정렬 유지 */
           border-bottom-right-radius: 4px;
       }
       .chat-bubble.other {
           background-color: #f0f0f0;
           color: #333;
           /* ★★★ margin-right: auto; 제거 또는 초기화 (왼쪽 끝에 붙도록 강제) ★★★ */
           margin-right: 0;
           border-bottom-left-radius: 4px;
       }
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
let lastMessageId = 0;
let POLLING_INTERVAL = 3000; // 3초 (3000ms)

/**
 * 특정 화면 파일을 로드하고 네비게이션을 업데이트합니다.
 * @param {string} fileToLoad - 로드할 HTML 파일 경로 (예: 'apply.html')
 * @param {string} tabId - 활성화할 탭의 ID (예: 'apply')
 * @param {object} [data={}] - 로드된 화면으로 전달할 추가 데이터
 */
function loadView(fileToLoad, tabId, data = {}) {
    currentFile = fileToLoad;
    $('#content-area').load(fileToLoad, function() {
        // 로드 성공 후 탭 활성화 상태 업데이트
        $('#main-nav .tab-button').removeClass('active bg-primary text-white hover:bg-gray-100').addClass('hover:bg-gray-100');
        $(`#main-nav button[data-id="${tabId}"]`).removeClass('hover:bg-gray-100').addClass('active bg-primary text-white');

        // 로드된 화면에 따라 특정 로직 바인딩 함수 호출
        bindViewLogic(tabId, data);
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
 * @param {string} tabId - 활성화할 탭의 ID
 * @param {object} [data={}] - loadView에서 전달받은 데이터
 * AJAX 주석은 여기에 위치합니다.
 */
function bindViewLogic(tabId, data = {}) {

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

            // **AJAX 주석: 필터링된 상담 리스트 데이터 로드** +${filter}`
             $.ajax({
                 url: 'http://localhost:8080/counsel/history/testId', type: 'GET',
                 contentType: 'application/json',
                 success: function(data) { /* 리스트 업데이트 로직 */
                    renderConsultationList(data.data, filter);
                 }
             });
        });

        // 리스트 항목 클릭 시 채팅 화면으로 이동
        $('#consultation-list').on('click', '.consultation-item', function() {
            const consultationId = $(this).find('#counsel-id').text().trim();
            loadView('chat.html', 'chat', { counselId: consultationId });

        });
    }

    else if (tabId === 'chat') {

        if (data.counselId) {
             $.ajax({
                 url: "http://localhost:8080/chat?counselId="+data.counselId,
                 type: 'GET',
                 success: function(response) {
                    renderChatMessages(response.data, data.counselId);
                 },
                  error: function(xhr) {
                       console.error(`[${data.counselId}] 채팅 내역 로드 실패:`, xhr);
                  }
             });
        } else {
                    console.error("채팅 화면 로드 실패: counselId가 전달되지 않았습니다.");
        }

        // 채팅 전송 로직
        $('#send-chat-btn').on('click', function() { sendMessage('chat-input', '#chat-messages','U'); });
        $('#chat-input').on('keypress', function(e) {
            if (e.which == 13) { sendMessage('chat-input', '#chat-messages','U'); }
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
            $.ajax({
                 url: 'http://localhost:8080/counsel', type: 'GET',
                 contentType: 'application/json',
                 success: function(data) { /* 리스트 업데이트 로직 */
                    renderCounselorList(data.data, filter);
                 }
             });
        });

        // 리스트 항목 클릭 시 상세 채팅 화면으로 이동
        $('#counselor-consultation-list').on('click', '.consultation-item', function() {
            const counselId = $(this).data('counsel-id'); // 동적으로 생성된 data-counsel-id 속성 사용

            // counselor_chat_detail.html을 메인 영역에 직접 로드하고 ID를 전달
            $('#content-area').load('counselor_chat_detail.html', function() {
                // 상세 화면 로드 후 로직 바인딩 및 ID 전달
                bindViewLogic('counselor-chat-detail', { counselId: counselId });
            });
        });
    }

    else if (tabId === 'counselor-chat-detail') {
       const counselId = data.counselId; // loadView에서 전달받은 counselId
       if (counselId) {
           $.ajax({
                url: "http://localhost:8080/chat?counselId="+counselId,
                type: 'GET',
                success: function(response) {
                   console.log(`[${counselId}] 채팅 내역 로드 성공:`, response);

                   // 응답 전체 객체를 전달하여 렌더링 함수 호출
                   renderCounselorChatMessages(response, counselId);
                },
                 error: function(xhr) {
                      console.error(`[${counselId}] 채팅 내역 로드 실패:`, xhr);
                      $('#counselor-chat-messages').html('<div class="text-center text-red-500 mt-10">채팅 내역을 불러오는 데 실패했습니다.</div>');
                 }
            });
       } else {
           console.error("채팅 화면 로드 실패: counselId가 전달되지 않았습니다.");
       }

       // 리스트로 돌아가기 (기존 코드 유지)
       $('#back-to-list').on('click', function() {
           loadView('counselor_list.html', 'counselor-list');
       });

       // 채팅 전송 로직 (기존 코드 유지)
       $('#counselor-chat-detail-input-btn').on('click', function() { sendMessage('counselor-chat-detail-input-field', '#counselor-chat-messages','C'); });
       $('#counselor-chat-detail-input-field').on('keypress', function(e) {
           if (e.which == 13) { sendMessage('counselor-chat-detail-input-field', '#counselor-chat-messages','C'); }
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
function sendMessage(inputId, containerId, senderType) {
    const counselId = $('#counsel-id').text();
    const input = $(`#${inputId}`);
    const message = input.val().trim();
    const messagesContainer = $(containerId);
    console.log(messagesContainer);

    if (message) {
        input.val('');
        messagesContainer.scrollTop(messagesContainer[0].scrollHeight);

        // **AJAX 주석: 채팅 메시지 서버로 전송**
         $.ajax({
             url: 'http://localhost:8080/chat', type: 'POST',
             contentType: 'application/json',
             data: JSON.stringify({
                 counselId: counselId,
                 senderId: senderType === 'U' ? "user2" : "counselor1",
                 senderType: senderType,
//                 senderId: "user2",
//                 senderType: "U",
                 msg: message
                 }),
             success: function(response) {
                poll();
             }
         });
    }
}
function poll() {
    const counselId = $('#counsel-id').text();

    // counselId 유효성 검사
    if (!counselId) {
        console.error('counselId가 없습니다.');
        return;
    }

    fetch(`http://localhost:8080/chat/poll?counselId=${counselId}&lastMessageId=${lastMessageId}`)
       .then(res => {
           if (!res.ok) {
               throw new Error(`HTTP error! status: ${res.status}`);
           }
           return res.json();
       })
       .then(list => {
           console.log('Poll response:', list); // 디버깅용

           if (list && list.length > 0) {
               list.forEach(msg => {
                   addMsg(msg);
                  if (!lastMessageId || msg.messageId > lastMessageId) {
                      lastMessageId = msg.messageId;
                  }
               });
           }
          setTimeout(poll, 3000);
       })
       .catch(error => {
           console.error('Polling error:', error);
           setTimeout(poll, 3000);
       });

}


// XSS 방지를 위한 HTML 이스케이프 함수
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// 메시지를 화면에 추가하는 함수
function addMsg(msg) {

    const messagesContainerId = currentRole === 'customer' ? 'chat-messages' : 'counselor-chat-messages';
    const chatMessages = document.getElementById(messagesContainerId);

    if (!chatMessages) {
        console.error('chat-messages 요소를 찾을 수 없습니다.');
        return;
    }

    const messageDiv = document.createElement('div');

    // 현재 시간
    const now = new Date();
    const timeStr = now.getHours().toString().padStart(2, '0') + ':' +
                    now.getMinutes().toString().padStart(2, '0');

    // 메시지 정렬 결정 로직
    let isMine = false;

    if (currentRole === 'customer') {
        // 고객 화면: sender가 'U'면 내 메시지
        isMine = (msg.senderType === 'U');
    } else {
        // 상담사 화면: sender가 'C'이면 내 메시지
        isMine = (msg.senderType === 'C');
    }

    if (isMine) {
        // 내가 보낸 메시지 (오른쪽 정렬)
        messageDiv.className = 'flex flex-col items-end';
        messageDiv.innerHTML = `
            <div class="chat-bubble mine">${escapeHtml(msg.msg)}</div>
            <div class="text-xs text-gray-500">${timeStr}</div>
        `;
    } else {
        // 상대방이 보낸 메시지 (왼쪽 정렬)
        const senderName = currentRole === 'customer' ? '김상담' : '고객';
        const avatar = currentRole === 'customer' ? '👩‍💼' : '👤';

        messageDiv.className = 'flex items-start';
        messageDiv.innerHTML = `
            <div class="w-8 h-8 rounded-full bg-gray-200 mr-3 flex items-center justify-center">
                <span role="img" aria-label="아바타">${avatar}</span>
            </div>
            <div>
                <div class="text-sm font-medium">${senderName}</div>
                <div class="chat-bubble other">${escapeHtml(msg.msg)}</div>
                <div class="text-xs text-gray-500">${timeStr}</div>
            </div>
        `;
    }

    chatMessages.appendChild(messageDiv);

    // 스크롤을 맨 아래로
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// --- HTML을 생성하여 화면을 갱신하는 함수 ---
function renderConsultationList(data, currentFilter) {
    const listContainer = $('#consultation-list');
    let html = '';

    if (!data || data.length === 0) {
        listContainer.html('<div class="p-4 text-center text-gray-500 border rounded-lg bg-white">상담 내역이 없습니다.</div>');
        return;
    }

    data.forEach(item => {
        // 필터링 (AJAX URL에 필터를 포함하지 않았다면 프론트에서 필터링)
        if (currentFilter !== 'all' && item.status !== currentFilter) {
            return; // 현재 필터와 맞지 않으면 스킵
        }

        // 상태값에 따른 스타일 클래스 결정
        let statusClass = '';
        let statusText = '';
        switch(item.status) {
            case 'R': statusClass = 'status-waiting'; statusText = '대기중'; break;
            case 'I': statusClass = 'status-progress'; statusText = '진행중'; break;
            case 'E': statusClass = 'status-done'; statusText = '완료'; break;
            default: statusClass = 'status-done'; statusText = '알 수 없음';
        }

        // 항목별 HTML 생성 (list.html의 구조를 기반)
        html += `
            <div class="p-4 border rounded-lg shadow-sm bg-white cursor-pointer hover:bg-gray-50 consultation-item" data-status="${item.status.toLowerCase()}">
                <div class="flex justify-between items-start mb-2">
                    <div id="counsel-id" style="display: none;">${item.id}</div>
                    <div class="font-bold">${item.id}</div>
                    ${item.unreadCount ? `<span class="text-red-500 font-bold text-lg">${item.unreadCount}</span>` : ''}
                </div>
                <div class="flex items-center mb-2">
                    <span class="status-tag ${statusClass} mr-2">${statusText}</span>
                    <span class="text-sm text-gray-700">상담사: ${item.counselorId || '미정'}</span>
                </div>
                <div class="text-gray-600 mb-2">${item.category}</div>
                <div class="text-sm text-gray-500 flex justify-between">
                    <div>${item.lastMessage || '새로운 메시지가 없습니다.'}</div>
                    <div>${item.timestamp || ''}</div>
                </div>
            </div>
        `;
    });

    listContainer.html(html);
}
/**
 * 상담사 리스트 데이터를 받아 #counselor-consultation-list 영역을 갱신합니다.
 * @param {Array} data - 백엔드에서 받은 상담 목록 배열 (data.content)
 * @param {string} currentFilter - 현재 적용된 필터 (R, I, E, all)
 */
function renderCounselorList(data, currentFilter) {
    const listContainer = $('#counselor-consultation-list');
    let html = '';

    // 데이터가 없거나 content 배열이 비어있는 경우
    if (!data || !data.content || data.content.length === 0) {
        listContainer.html('<div class="p-4 text-center text-gray-500 border rounded-lg bg-white">현재 상담 내역이 없습니다.</div>');
        return;
    }

    // 1. 데이터 순회 및 HTML 생성
    data.content.forEach(item => {
        // 백엔드에서 필터링되지 않았다면, 여기서 필터링
        if (currentFilter !== 'all' && item.status !== currentFilter) {
            return;
        }

        // 상태값에 따른 스타일 및 텍스트 결정
        let statusClass = '';
        let statusText = '';
        switch(item.status) {
            case 'R': statusClass = 'status-waiting'; statusText = '대기중'; break; // R: 접수/대기중
            case 'I': statusClass = 'status-progress'; statusText = '진행중'; break; // I: In Progress (진행중)
            case 'E': statusClass = 'status-done'; statusText = '완료'; break;    // E: End (완료)
            default: statusClass = 'status-done'; statusText = '알 수 없음';
        }

        // 아이템에서 필요한 데이터 추출 및 기본값 설정
        // counselorId는 상담사 화면이므로, userId를 고객명으로 사용
        const customerName = item.userName || '고객 ' + item.userId.substring(0, 8); // 예시로 userId 일부 사용
        const category = item.category || '상담 분야 미정';
        const lastMessage = item.lastMessage || '새로운 메시지가 없습니다.';
        const timeAgo = item.timeAgo || '방금 전'; // 백엔드에서 "5분 전"과 같은 값을 전달한다고 가정
        const unreadCount = item.unreadCount || 0;

        // 아바타 이모지는 예시로 고정
        const avatar = item.avatar || '🧑';

        // 2. 항목별 HTML 생성 (제공된 HTML 구조 기반)
        html += `
            <div class="p-4 border rounded-lg shadow-sm bg-white flex justify-between items-center cursor-pointer hover:bg-gray-50 consultation-item"
                 data-status="${item.status.toLowerCase()}" data-counsel-id="${item.id}">
                <div class="flex items-start">
                    <div class="w-10 h-10 rounded-full bg-gray-200 mr-4 flex items-center justify-center">
                        <span role="img" aria-label="고객 아바타">${avatar}</span>
                    </div>
                    <div>
                        <div class="font-bold text-lg flex items-center">${customerName} <span class="status-tag ${statusClass} ml-2">${statusText}</span></div>
                        <div class="text-gray-600 font-medium">${category}</div>
                        <div class="text-sm text-gray-500">${lastMessage}</div>
                    </div>
                </div>
                <div class="flex flex-col items-end">
                    <div class="text-sm text-gray-500 mb-1">${timeAgo}</div>
                    ${unreadCount > 0 ? `<span class="bg-red-500 text-white w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold">${unreadCount}</span>` : ''}
                </div>
            </div>
        `;
    });

    // 3. 화면 갱신
    listContainer.html(html);
}
function renderChatMessages(messages, counselId) {
    $('#counsel-id').text(counselId);
    const messagesContainer = $('#chat-messages');
    messagesContainer.empty(); // 기존 메시지 모두 지우기

    // messages 배열을 순회하며 HTML 생성 (예시)
    if (messages && messages.length > 0) {
        messages.forEach(msg => {
            // 서버에서 받은 데이터에 따라 'mine' 또는 'other' 클래스 적용
            const isMine = msg.senderType === 'U'; // 'U'는 사용자, 'C'는 상담사라고 가정
            const bubbleClass = isMine ? 'mine' : 'other';
            const alignment = isMine ? 'items-end' : 'items-start';

            const chatHtml = `
                <div class="flex flex-col ${alignment}">
                    <div class="chat-bubble ${bubbleClass}">${msg.msg}</div>
                    <div class="text-xs text-gray-500">${msg.regDate}</div>
                </div>
            `;
            messagesContainer.append(chatHtml);
        });
        messagesContainer.scrollTop(messagesContainer[0].scrollHeight); // 스크롤 하단으로 이동
    } else {
        messagesContainer.html('<div class="text-center text-gray-500">대화 내용이 없습니다. 새로운 메시지를 보내세요.</div>');
    }
}
function renderCounselorChatMessages(response, counselId) {
    $('#counsel-id').text(counselId);
    const messages = response.data;
    const messagesContainer = $('#counselor-chat-messages');
    messagesContainer.empty();

    if (!messages || messages.length === 0) {
        messagesContainer.html('<div class="text-center text-gray-500 mt-10">대화 내용이 없습니다.</div>');
        return;
    }

    messages.forEach(msg => {
        const isCounselor = msg.senderType === 'C';
        const bubbleClass = isCounselor ? 'mine' : 'other';
        const alignmentWrapper = isCounselor ? 'justify-end' : 'justify-start';

        let displayTime = '';
        if (msg.regDate && msg.regDate.length >= 14) {
             const timePart = msg.regDate.substring(8, 12);
             displayTime = `${timePart.substring(0, 2)}:${timePart.substring(2, 4)}`;
        }

        let chatHtml = '';

        if (isCounselor) {
            // 1. 상담사 (Mine)
            chatHtml = `
                <div class="flex ${alignmentWrapper} w-full mb-2"> <div class="flex flex-col items-end">
                    <div class="text-xs text-gray-500 mb-1">${displayTime}</div>
                    <div class="chat-bubble ${bubbleClass}">
                        ${msg.msg}
                    </div>
                </div>
            </div>
        `;
        } else {
                // 2. 고객 (Other)
                chatHtml = `
                    <div class="flex flex-col ${alignmentWrapper} w-full mb-2 flex-grow-0">
                        <div class="flex items-center mb-1">
                            <div class="w-8 h-8 rounded-full bg-gray-200 mr-2 flex items-center justify-center flex-shrink-0">
                                <span role="img" aria-label="고객 아바타">🐱</span>
                            </div>
                            <div class="text-sm text-gray-500">${displayTime}</div>
                        </div>

                        <div class="flex justify-start pl-10 flex-shrink-0">
                            <div class="chat-bubble ${bubbleClass}">
                                ${msg.msg}
                            </div>
                        </div>
                    </div>
                `;
            }

        messagesContainer.append(chatHtml);
    });

    messagesContainer.scrollTop(messagesContainer[0].scrollHeight);
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

document.addEventListener('DOMContentLoaded', function () {
    console.log('Polling started');
    poll();
});
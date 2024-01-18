const daysContainer = document.querySelector(".days"),
    nextBtn = document.querySelector(".next-btn"),
    prevBtn = document.querySelector(".prev-btn"),
    todayBtn = document.querySelector(".today-btn"),
    mouth = document.querySelector(".mouth")

const mouths = [
    'Tháng 1',
    'Tháng 2',
    'Tháng 3',
    'Tháng 4',
    'Tháng 5',
    'Tháng 6',
    'Tháng 7',
    'Tháng 8',
    'Tháng 9',
    'Tháng 10',
    'Tháng 11',
    'Tháng 12'
];
const day = [
    'Thứ 2',
    'Thứ 3',
    'Thứ 4 ',
    'Thứ 5',
    'Thứ 6',
    'Thứ 7',
    'Chủ nhật'
]

const date = new Date();

let currentMouth = date.getMonth();

let currentYear = date.getFullYear();

function renderCalendar() {
    date.setDate(1);
    const firstDay = new Date(currentYear, currentMouth, 1);
    const lastDay = new Date(currentYear, currentMouth + 1, 0);
    const lastDayIndex = lastDay.getDay();
    const lastDayDate = lastDay.getDate();
    const prevLastDay = new Date(currentYear, currentMouth, 0);
    const prevLastDayDate = prevLastDay.getDate();
    const nextDays = 7 - lastDayIndex - 1;

    mouth.innerHTML = `${mouths[currentMouth]} ${currentYear}`;
    console.log(date);
    // update days
    let days = "";

    for (let x = firstDay.getDay(); x > 0; x--) {
        days += `<div class="day prev">${prevLastDayDate - x + 1}</div>`
    }

    for (let i = 1; i <= lastDayDate; i++) {
        if (i === new Date().getDate() &&
            currentMouth === new Date().getMonth() &&
            currentYear === new Date().getFullYear()
        ) {
            days += `<div class="day today" data-bs-toggle="modal" data-bs-target="#exampleModal">${i}</div>`
        } else {
            days += `<div class="day" data-bs-toggle="modal" data-bs-target="#exampleModal">${i}</div>`

        }
    }

    // next mouth days
    for (let j = 1; j <= nextDays; j++) {
        days += `<div class="day next">${j}</div>`
    }
    hideTodayBtn();
    daysContainer.innerHTML = days;
}

renderCalendar();

nextBtn.addEventListener("click", () => {
    currentMouth++;
    if (currentMouth > 11) {
        currentMouth = 0;
        currentYear++;
    } else {
        renderCalendar();
    }
});
prevBtn.addEventListener("click", () => {
    currentMouth--;
    if (currentMouth < 0) {
        currentMouth = 11;
        currentYear--;
    } else {
        renderCalendar();
    }
})

todayBtn.addEventListener("click", () => {
    currentMouth = date.getMonth();
    currentYear = date.getFullYear();
    renderCalendar();
});

function hideTodayBtn() {
    if (
        currentMouth === new Date().getMonth() &&
        currentYear === new Date().getFullYear()
    ) {
        todayBtn.style.display = "none";
    } else {
        todayBtn.style.display = "flex";
    }
}

const dayElements = document.querySelectorAll('.day');
var itemMap = {}
var startTime = undefined;
let inputValues2 = {};
dayElements.forEach(day => {
    day.addEventListener('click', () => {
        const selectedDay = day.textContent;
        const selectedRoom = document.getElementById("roomName");
        const a = `${currentYear}-${currentMouth + 1}-${selectedDay}`;
        const h1Element = document.getElementById('selectedDate');
        h1Element.textContent = ' Ngày: ' + formatCustomDateH1(a);
        var date1 = '';
        fetch('http://localhost:8080/schedule/search-by-date?date1=' + a + '&room=' + selectedRoom.value)
            .then(response => {
                console.log(response)
                if (!response.ok) {
                    date1 = a;
                    console.log(date1 + 'hehehhe');
                }
                return response.json();
            })
            .then(data => {
                console.log(data)
                itemMap = {};
                startTime = undefined;
                let scheduleModal = document.getElementById("big-task-container");
                scheduleModal.innerHTML = '';
                data.forEach(function (item, index) {
                    itemMap[item.id] = item;
                    if (index === 0) startTime = item.startAt;
                    const newRowHTML = `<div class="task-item" id="${item.id}">
                            <div class="row">
                                <div class="col-5">
                                    ${item.movie.name}
                                </div>
                                <div class="col-3">
                                    Thời gian: ${formatCustomDate2(item.startAt)}- ${formatCustomDate2(item.finishAt)} 
                                </div>
                                <div class="col-4">
                                    Giá: <input type="text" class="form-control" style="width: 90%;margin-left: 40px;margin-top: -30px; padding-top: -20px;margin-bottom: 28px;" value="${item.price}" onchange="updateItemPrice('${item.id}', this)"/>
                                </div>
                            </div>                        
                    </div>`;
                    convertToISOString(item.startAt);
                    item.operatingStatus = 1;
                    scheduleModal.innerHTML += newRowHTML;

                })
                Object.keys(inputValues2).forEach(function (itemI) {
                    document.getElementById('price2').value = inputValues2[itemI];
                    console.log(inputValues2[itemI] + 'kiki')
                });
            })
            .catch(error => {
                console.log('fetch error:', error);
            });
    });
})
console.log(itemMap)

function sendItemMapToServer() {
    const url = 'http://localhost:8080/schedule/update-all';
    const bodyData = Object.values(itemMap);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bodyData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Lỗi mạng hoặc lỗi máy chủ khi gửi dữ liệu');
            }
            return response.json(); // hoặc `response.text()` nếu máy chủ trả về dữ liệu dạng text.
        })
        .then(data => {
            console.log('Dữ liệu nhận được từ server sau khi gửi itemMap:', data);
        })
        .catch(error => {
            console.error('Có lỗi xảy ra khi gửi itemMap:', error);
        });
}

function addItemAtIndex(obj, keyToAdd, valueToAdd, index) {
    if (index === Object.keys(obj).length) {
        return {
            ...obj,
            [keyToAdd]: valueToAdd
        };
    } else {
        return Object.fromEntries(
            Object.entries(obj)
                .reduce((acc, [key, value], i) => {
                    if (i === index) {
                        acc.push([keyToAdd, valueToAdd]);
                    }
                    acc.push([key, value]);
                    return acc;
                }, [])
        );
    }
}


function formatCustomDate(inputString) {
    // Chuyển đổi chuỗi thành đối tượng Date
    let dateObject = new Date(inputString);

    // Lấy thông tin từ đối tượng Date
    let day = String(dateObject.getDate()).padStart(2, '0');
    let month = String(dateObject.getMonth() + 1).padStart(2, '0');
    let year = dateObject.getFullYear();
    let hours = String(dateObject.getHours()).padStart(2, '0');
    let minutes = String(dateObject.getMinutes()).padStart(2, '0');
    let seconds = String(dateObject.getSeconds()).padStart(2, '0');
    // Xây dựng chuỗi định dạng mới
    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

function formatCustomDate2(inputString) {
    // Chuyển đổi chuỗi thành đối tượng Date
    let dateObject = new Date(inputString);

    // Lấy thông tin từ đối tượng Date
    let day = String(dateObject.getDate()).padStart(2, '0');
    let month = String(dateObject.getMonth() + 1).padStart(2, '0');
    let year = dateObject.getFullYear();
    let hours = String(dateObject.getHours()).padStart(2, '0');
    let minutes = String(dateObject.getMinutes()).padStart(2, '0');
    let seconds = String(dateObject.getSeconds()).padStart(2, '0');
    return `${hours}:${minutes}`;
}

function formatCustomDateH1(inputString) {
    // Chuyển đổi chuỗi thành đối tượng Date
    let dateObject = new Date(inputString);

    // Lấy thông tin từ đối tượng Date
    let day = String(dateObject.getDate()).padStart(2, '0');
    let month = String(dateObject.getMonth() + 1).padStart(2, '0');
    let year = dateObject.getFullYear();
    let hours = String(dateObject.getHours()).padStart(2, '0');
    let minutes = String(dateObject.getMinutes()).padStart(2, '0');
    let seconds = String(dateObject.getSeconds()).padStart(2, '0');
    // Xây dựng chuỗi định dạng mới
    return `${day}/${month}/${year}`;
}


function convertToISOString(inputString) {
    // Phân tích chuỗi thành các thành phần ngày giờ
    let [day, month, year, time] = inputString.split(/[\/\s:]+/);

    // Tạo đối tượng Date từ các thành phần
    let dateObject = new Date(`${year}-${month}-${day}T${time}`);


    return dateObject;
}

const drake = dragula([document.getElementById("big-task-container")]);

drake.on("drag", function (el, source) {
    el.classList.add("dragging");
});

drake.on("dragend", function (el) {
    el.classList.remove("dragging");
});
let inputValues = {};
/////////////////////////
document.addEventListener("input", function(event) {
    const { target } = event;
    if (target.matches('.price-input')) {
        const id = target.getAttribute("data-id");
        const value = target.value;
        inputValues[id] = value;
    }
});
/////////////////////////////////
drake.on("drop", function (el, target, source, sibling) {
    let items = Array.from(target.children);
    let draggedItemIndex = items.indexOf(el);
    let selectedItem = itemMap[el.id];
    console.log(draggedItemIndex);
    delete itemMap[el.id];

    itemMap = addItemAtIndex(itemMap, el.id, selectedItem, draggedItemIndex);
    let scheduleModal = document.getElementById("big-task-container");
    scheduleModal.innerHTML = '';
    const nextStartTime = new Date(startTime);
    const finishAt = new Date(startTime);
    Object.values(itemMap).forEach(function (item, index) {
        item['startAt'] = formatCustomDate(new Date(nextStartTime));
        item['finishAt'] = formatCustomDate(finishAt.setMinutes(nextStartTime.getMinutes() + item.movie.movieDuration + 15));
        const newRowHTML = `<div class="task-item" id="${item.id}">
                            <div class="row">
                                <div class="col-5">
                                    ${item.movie.name}
                                </div>
                                <div class="col-3">
                                    Thời gian: ${formatCustomDate2(item.startAt)}- ${formatCustomDate2(item.finishAt)} 
                                </div>
                                <div class="col-4">
                                    Giá: <input type="text" class="form-control" style="width: 90%;margin-left: 40px;margin-top: -30px; padding-top: -20px;margin-bottom: 28px;" value="${item.price}" onchange="updateItemPrice('${item.id}', this)"/>
                                </div>
                            </div>                     
                    </div>`;

        scheduleModal.innerHTML += newRowHTML;
        nextStartTime.setMinutes(nextStartTime.getMinutes() + item.movie.movieDuration + 15);
    })
    Object.keys(inputValues).forEach(function (itemId) {
        const inputElement = document.querySelector(`input[data-id="${itemId}"]`);
        if (inputElement) {
            inputElement.value = inputValues[itemId];
        }
        console.log(inputValues[itemId])
    });
});

function updateItemPrice(itemId, element) {
    console.log(element.value);
    itemMap[itemId].price = element.value;
}

////////////////////////////////////////////////////////////

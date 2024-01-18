const rowsPerPage = 15;
// Các hàng dữ liệu trong bảng
const tableRows = document.querySelectorAll('tbody tr');
// Tính số trang dựa trên số hàng dữ liệu
const totalPagess = Math.ceil(tableRows.length / rowsPerPage);
let currentPagee = 1; // Trang hiện tại

showPage(1); // Hiển thị trang đầu tiên khi trang web được tải
// Tạo và thêm nút phân trang vào phần tử có lớp là "pagination"
const pagination = document.querySelector('.pagination');
updatePagination(currentPagee);

function updatePagination(currentPagee) {
    pagination.innerHTML = ''; // Xóa hết các button cũ
    const maxButtons = 5; // Số lượng button tối đa được hiển thị
    const startPage = Math.max(1, currentPagee - Math.floor(maxButtons / 2));
    const endPage = Math.min(totalPagess, startPage + maxButtons - 1);

    if (startPage > 1) {
        const previousPage = document.createElement('button');
        previousPage.innerText = '<';
        previousPage.addEventListener('click', function() {
            currentPagee = Math.max(1, currentPagee - 1); // Di chuyển đến trang trước đó
            showPage(currentPagee);
            updatePagination(currentPagee);
        });
        pagination.appendChild(previousPage);
    }

    for (let i = startPage; i <= endPage; i++) {
        const button = document.createElement('button');
        button.innerText = i;
        button.addEventListener('click', function() {
            currentPagee = i;
            showPage(currentPagee);
            updatePagination(currentPagee);
        });
        if (i === currentPagee) {
            button.classList.add('active');
            button.style.backgroundColor = 'darkgray';
        }
        pagination.appendChild(button);
    }
    if (endPage < totalPagess) {
        const nextPage = document.createElement('button');
        nextPage.innerText = '>';
        nextPage.addEventListener('click', function() {
            currentPagee = Math.min(totalPagess, currentPagee + 1); // Di chuyển đến trang kế tiếp
            showPage(currentPagee);
            updatePagination(currentPagee);
        });
        pagination.appendChild(nextPage);
    }

}

// Hiển thị các hàng dữ liệu tương ứng với trang được chọn
function showPage(pageNumber) {
    const startIndex = (pageNumber - 1) * rowsPerPage;
    const endIndex = startIndex + rowsPerPage;
    tableRows.forEach((row, index) => {
        if (index >= startIndex && index < endIndex) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}
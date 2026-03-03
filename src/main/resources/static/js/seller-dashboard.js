(function () {
    function updateSellerNotifications() {
        fetch('/seller/notifications/new-orders', { credentials: 'same-origin' })
            .then(function (response) { return response.json(); })
            .then(function (data) {
                var newCount = Number(data.newOrderCount || 0);
                var pendingCount = Number(data.pendingOrderCount || 0);

                var badge = document.getElementById('sidebarNewOrderBadge');
                if (badge) {
                    badge.textContent = String(newCount);
                    badge.style.display = newCount > 0 ? 'inline-block' : 'none';
                }

                var newText = document.getElementById('newOrderCountText');
                if (newText) {
                    newText.textContent = String(newCount);
                }

                var pendingText = document.getElementById('pendingOrderCountText');
                if (pendingText) {
                    pendingText.textContent = String(pendingCount);
                }
            })
            .catch(function () {});
    }

    setInterval(updateSellerNotifications, 15000);
})();

 document.addEventListener('DOMContentLoaded', function () {
        var modeInput = document.getElementById('modeHiddenInput');
        var busOperatorIdInput = document.getElementById('idInput');
        var updatedBusOperatorElement = document.getElementById('hiddenBusOperatorId');
        if (updatedBusOperatorElement) {
            showUpdateSuccessAndRedirect(updatedBusOperatorElement.value);
        }

    if(modeInput){
        const form = document.getElementById('operatorForm');
            var mode = modeInput.value;
            if(busOperatorIdInput){
              var busOperatorId = busOperatorIdInput.value;
            }
            else{
               console.log("busOperatorId not found in the DOM");
            }

        if (mode === 'add') {
            form.action = '/bookMyBus/addBusOperator';
            form.method = 'post' ;
               }

        if (mode === 'update') {
            busOperatorIdInput.setAttribute('readonly', true); // Make the field readonly
            busOperatorIdInput.style.backgroundColor = 'lightgray';
            form.action = '/bookMyBus/busOperatorPortal/' + busOperatorId + '/update';
            form.method = 'post' ;
        }
        if (mode === 'view') {
             var inputs = document.querySelectorAll('input');
            // Disable each input
                inputs.forEach(function(input) {
//                    console.log("Disabling input: " + input.name);  // Debugging each input
                    input.disabled = true;
                });

            var textAreas = document.querySelectorAll('textarea');
                    textAreas.forEach(function(textarea) {
//                        console.log("Disabling textarea: " + textarea.name);  // Debugging each textarea
                        textarea.disabled = true;
                        });

            form.method = 'get';
            form.action = '/bookMyBus/busOperatorPortal/' + busOperatorId;
        }

    }else {
          console.log("Mode not found in the DOM");
      }

});
    // Function to show the success notification and redirect
    function showUpdateSuccessAndRedirect(busOperatorId) {

        // Show the notification box
        document.getElementById('notificationBox').style.display = 'block';
        // Hide notification  after a few seconds (e.g., 3 seconds)  and then redirect
        setTimeout(function () {
            window.location.href = "/bookMyBus/busOperatorPortal/" + busOperatorId;
        }, 5000);  // 5000 milliseconds = 5 seconds
    }
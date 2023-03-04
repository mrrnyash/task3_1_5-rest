// Get all users for table
async function fillUsersTable() {
    const allUsersTable = document.getElementById('allUsersTable')
    console.warn('Start "listUsersTable" function')

    $('#allUsersTable').empty();

    const response = await fetch('/users', {
        method: "GET",
        headers: {"Accept": "application/json"}
    });
    
}






const allUsersTable = $('#allUsersTable')
let editModalForm = document.forms['editModalForm']
let deleteModalForm = document.forms['deleteModalForm']
let newUserForm = document.forms['newUserForm']

$(async function () {
    await fillUsersTable()
    getAuthenticatedUser()
    addUser()
    editUser()
    deleteUser()
})


// All Users Table
function fillUsersTable() {
    allUsersTable.empty()
    fetch('/api/users')
        .then(res => res.json())
        .then(data =>
            data.forEach(user => {
                    let tableRow = `$(
                    <tr>
                            <td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>   
                            <td>${user.email}</td>
                            <td>${user.roles.map(role => ' ' + role.authority.substring(5))}</td>
                            <td>
                                <button type="button" class="btn btn-info"
                                data-bs-toogle="modal"
                                data-bs-target="#editModal"
                                onclick="editModalData(${user.id})">Edit</button>
                            </td>
                            <td>
                                <button type="button" class="btn btn-danger" 
                                data-toggle="modal"
                                data-bs-target="#deleteModal"
                                onclick="deleteModalData(${user.id})">Delete</button>
                            </td>
                        </tr>)`
                    allUsersTable.append(tableRow)
                }
            ))
}

// Edit User
async function editModalData(id) {
    const modal = new bootstrap.Modal(document.querySelector('#editModal'))
    await openModal(editModalForm, modal, id)
}

function editUser() {
    editModalForm.addEventListener('submit', event => {
        event.preventDefault()
        let editedUserRoles = []
        for (let i = 0; i < editModalForm.roles.options.length; i++) {
            if (editModalForm.roles.options[i].selected) {
                editedUserRoles.push({
                    id: editModalForm.roles.value,
                    authority: 'ROLE_' + editModalForm.roles.options[i].text
                })
            }
        }
        fetch('/api/users', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: editModalForm.id.value,
                firstName: editModalForm.firstName.value,
                lastName: editModalForm.lastName.value,
                age: editModalForm.age.value,
                email: editModalForm.email.value,
                password: editModalForm.password.value,
                roles: editedUserRoles
            })
        }).then(() => {
            $('#editModalCloseButton').click()
            fillUsersTable()
        })
    })
}

// Delete User
function deleteUser() {
    deleteModalForm.addEventListener("submit", e => {
        e.preventDefault()
        fetch("/api/users/" + deleteModalForm.id.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
            $('#deleteModalCloseButton').click()
            fillUsersTable()
        })
    })
}

async function deleteModalData(id) {
    const modal = new bootstrap.Modal(document.querySelector('#deleteModal'))
    await openModal(deleteModalForm, modal, id)
    switch (deleteModalForm.roles.value) {
        case '1':
            deleteModalForm.roles.value = 'ADMIN'
            break
        case '2':
            deleteModalForm.roles.value = 'USER'
            break
    }
}

// Get User
async function getUser(id) {
    let response = await fetch('/api/users/' + id)
    return await response.json()
}

// Add New User
function addUser() {
    newUserForm.addEventListener('submit', event => {
        event.preventDefault()
        let newUserRoles = []
        for (let i = 0; i < newUserForm.roles.options.length; i++) {
            if (newUserForm.roles.options[i].selected) {
                newUserRoles.push({
                    id: newUserForm.roles.value,
                    authority: "ROLE_" + newUserForm.roles.options[i].text
                })
            }
        }
        fetch('/api/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: newUserForm.id.value,
                firstName: newUserForm.firstName.value,
                lastName: newUserForm.lastName.value,
                age: newUserForm.age.value,
                email: newUserForm.email.value,
                password: newUserForm.password.value,
                roles: newUserRoles
            })
        }).then(() => {
            newUserForm.reset()
            fillUsersTable()
            $('#users-tab').click()
        })
    })
}

// Open Modal
async function openModal(form, modal, id) {
    modal.show()
    let user = await getUser(id)
    form.id.value = user.id
    form.firstName.value = user.firstName
    form.lastName.value = user.lastName
    form.age.value = user.age
    form.email.value = user.email
    form.roles.value = user.roles[0].id
}

// Authenticated User
function getAuthenticatedUser() {
    fetch('/api/user')
        .then(res => res.json())
        .then(data => {
            $('#authenticatedUserEmail').append(data.email);
            let roles = data.roles.map(role => ' ' + role.authority.substring(5))
            $('#authenticatedUserRoles').append(roles)
            let user = `$(
                <tr>
                    <td>${data.id}</td>
                    <td>${data.firstName}</td>
                    <td>${data.lastName}</td>
                    <td>${data.age}</td>
                    <td>${data.email}</td>
                    <td>${roles}</td>
                </tr>)`
            $('#authenticatedUserTable').append(user)
        })
}









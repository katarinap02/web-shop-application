<template>
    <div class="tabela">
       
        <table>
            <tr>
                <th colspan="6">All users</th>
            </tr>
            <tr>
                <th>Name</th>
                <th>Surname</th>
                <th>Gender</th>
                <th>Birthdate</th>
                <th>Role</th>
                <th>Blocked</th>
            </tr>
            <tr v-for="u in allUsers" :key="u.username" :class="{ selected: selectedUser && selectedUser.username === u.username }" @click="selectUser(u)">
                <td>{{u.name}}</td>
                <td>{{u.surname }} </td>
                <td>{{formatGender(u.gender)}}</td>
                <td>{{u.birthDate}}</td>
                <td>{{formatRole(u.role)}}</td>
                <td>no</td>

            </tr>
        </table>
        
    </div>

   
</template>

<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const allUsers = ref('');
const router = useRouter();

const selectedUser = ref(null);

onMounted(() => {
    loadUsers();
})

function loadUsers()
{
    axios.get('http://localhost:8080/WebShopAppREST/rest/get').then(response => {
        if(response.data != "")
        {
            allUsers.value = response.data;
        }
        else {
            allUsers.value = '';
        }
    })
}

function formatGender(gender) {
      if (gender === 'FEMALE') {
        return 'female';
      } else if (gender === 'MALE') {
        return 'male';
      }
      return gender; // Default case
    }

function formatRole(role) {
      if (role === 'WORKER') {
        return 'worker';
      } else if (role === 'ADMINISTRATOR') {
        return 'administartor';
      }
      else if (role === 'MANAGER') {
        return 'manager';
      }
      else if (role === 'CUSTOMER') {
        return 'customer';
      }
      return role; // Default case
    }


function selectUser(us)
{
    this.selectedUser = us;
}

function blokSelectedUser()
{
    if(this.selectedFactory != null)
    {
        axios.get('http://localhost:8080/WebShopAppREST/rest/factories/delete?id=' +  this.selectedFactory.id).then(response =>
        {
            loadFactories();
            console.log(response.data);
        }
    )
        
    }
}







</script>

<style scoped>

template {
    height: 100%;
}

.tabela {
	color: #381d11;
	background-color: wheat;
	border-collapse: collapse;
	width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    width: 60%;
    
    
}

.header-content {
  display: flex;
  justify-content: center;
  align-items: center;
}


.title {
  flex-grow: 1; 
  text-align: center;
  margin-left: 100px;
}

.tabela th{
    padding: 8px;
	text-align: center;
	border-bottom: 1px solid #ddd;
    font-size: larger;
    
}
.tabela td {
	padding: 8px;
	text-align: center;
	border-bottom: 1px solid #ddd;
    font-size: larger;
}

.tabela .create {
    text-decoration: none;
    background-color: #5a086a;
    color: antiquewhite;
    margin-left: auto; 
}

.tabela th {
	background-color: #5a086a;
    color: antiquewhite;
    text-align: center;
}


.tabela tr:hover {
	background-color: beige;
}

.show-btn{
    background-color: #5a086a;
    border: #5a086a;
}

.press-btn{
    background-color: #5a086a;
    border: #5a086a;
    width: 130px;
    margin: 30px;
}

.selected {
    background-color: rgb(245, 195, 128); /* Change to your desired highlight color */
}

</style>
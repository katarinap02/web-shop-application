<template>
    
        <div class="sort">
            <h3>Sort</h3>
            <div>
                <label>Username: </label>
                <label>
            <input type="radio" name="sortUsername" value="ascending" v-model="sortUsername">
            Ascending
        </label>
        <label>
            <input type="radio" name="sortUsername" value="descending" v-model="sortUsername">
            Descending
        </label>
        <label>
            <input type="radio" name="sortUsername" value="unordered" v-model="sortUsername" >
            Unordered
        </label>
            </div>
            <div>
                <label>Name: </label>
                <label>
            <input type="radio" name="sortName" value="ascending" v-model="sortName">
            Ascending
        </label>
        <label>
            <input type="radio" name="sortName" value="descending" v-model="sortName">
            Descending
        </label>
        <label>
            <input type="radio" name="sortName" value="unordered" v-model="sortName">
            Unordered
        </label>
            </div>
            <div>
                <label>Surname: </label>
                <label>
            <input type="radio" name="sortSurname" value="ascending" v-model="sortSurname">
            Ascending
        </label>
        <label>
            <input type="radio" name="sortSurname" value="descending" v-model="sortSurname">
            Descending
        </label>
        <label>
            <input type="radio" name="sortSurname" value="unordered" v-model="sortSurname">
            Unordered
        </label>
            </div>

            <div>
                <label>Points: </label>
                <label>
            <input type="radio" name="sortPoints" value="ascending" v-model="sortPoints">
            Ascending
        </label>
        <label>
            <input type="radio" name="sortPoints" value="descending" v-model="sortPoints">
            Descending
        </label>
        <label>
            <input type="radio" name="sortPoints" value="unordered" v-model="sortPoints">
            Unordered
        </label>
            </div>
            
        </div>
        <div class="btn-container">
        <button class="btn btn-success press-btn1" @click.prevent="sort()">Sort</button>
        <button class="btn btn-success press-btn1" @click.prevent="refresh()">Refresh</button>
        </div>
<div class="tabela">
        <table>
            <tr>
                <th colspan="6">All users</th>
            </tr>
            <tr>
                <th>Name</th>
                <th>Surname</th>
                <th>Username</th>
                <th>Points</th>
                <th>Role</th>
                <th>Blocked</th>
            </tr>
            <tr v-for="u in allUsers" :key="u.username" :class="{ selected: selectedUser && selectedUser.username === u.username }" @click="selectUser(u)">
                <td>{{u.name}}</td>
                <td>{{u.surname }} </td>
                <td>{{u.username}}</td>
                <td>{{formatPoints(u.points)}}</td>
                <td>{{formatRole(u.role)}}</td>
                <td class="td1">{{formatBloked(u.bloked)}} <button v-show = "u.role !== 'ADMINISTRATOR'" class=" btn btn-success blokButton" @click.prevent="blokUser(u.username)">block</button></td>

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
const usernameData = ref(localStorage.getItem('userData'));
const selectedUser = ref(null);

const sortUsername = ref('unordered');
const sortName = ref('unordered');
const sortSurname = ref('unordered');
const sortPoints = ref('unordered');

onMounted(() => {
    loadUsers();
})

function refresh()
{
    loadUsers();
}

function loadUsers()
{
    axios.get('http://localhost:8080/WebShopAppREST/rest/get').then(response => {
        if(response.data != "")
        {
            allUsers.value = response.data.filter(user => user.username !== usernameData.value);
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
      return gender; 
    }

function formatPoints(point)
{
    if(point == -1)
    {
        return ""
    }
    else return point
}

function formatBloked(blok) {
      if (blok === true) {
        return 'yes';
      }
      return 'no'; 
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

function blokUser(username)
{
     axios.get('http://localhost:8080/WebShopAppREST/rest/blokUser?username=' +  username).then(response =>
        {
            loadUsers();
            console.log(response.data);
        }
    )
        
}

function sort()
{
    if(sortUsername.value === 'ascending')
        {
           
            allUsers.value.sort((a, b) => a.username.toLowerCase().localeCompare(b.username.toLowerCase())); 
        }
        else if(sortUsername.value === 'descending')
        {
            allUsers.value.sort((a, b) => b.username.toLowerCase().localeCompare(a.username.toLowerCase()))
        }

        if(sortSurname.value === 'ascending')
        {
           
            allUsers.value.sort((a, b) => a.surname.toLowerCase().localeCompare(b.surname.toLowerCase())); 
        }
        else if(sortSurname.value === 'descending')
        {
            allUsers.value.sort((a, b) => b.surname.toLowerCase().localeCompare(a.surname.toLowerCase()))
        }

        if(sortName.value === 'ascending')
        {
           
            allUsers.value.sort((a, b) => a.name.toLowerCase().localeCompare(b.name.toLowerCase())); 
        }
        else if(sortName.value === 'descending')
        {
            allUsers.value.sort((a, b) => b.name.toLowerCase().localeCompare(a.name.toLowerCase()))
        }

         
        if(sortPoints.value === 'ascending')
        {
           
           
            allUsers.value.sort((a, b) => a.points - b.points); 
        }
        else if(sortPoints.value === 'descending')
        {
            allUsers.value.sort((a, b) => b.points - a.points)
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

.blokButton {
    font-size: small; /* Adjust the font size to make the button smaller */
    background-color: #5a086a;
    border: none; /* Remove the border if not needed */
    padding: 3px 7px; /* Adjust padding for a smaller button */
    color: white; /* Set text color */
    align-items: center;
    position: absolute; /* Position the button absolutely within the <td> */
    top: 50%; /* Adjust as needed */
    right: 15px; /* Adjust as needed */
    transform: translateY(-50%);
}

.td1 {
    position: relative; 
    width: 170px;
}

.selected {
    background-color: rgb(245, 195, 128); /* Change to your desired highlight color */
}

.sort {
    font-family: Arial, sans-serif;
    text-align: left;
}

.sort h3 {
    font-size: 24px;
   
}

.sort div {
    margin-bottom: 10px;
}

.sort label {
    display: inline-block;
   
}

.sort label:first-child {
    font-weight: bold;
   
}



.sort label input[type="radio"] + label {
    font-weight: normal;
}


.press-btn1 {
    background-color: #5a086a;
    border: none;
    color: white;
    margin-right: 10px; /* Adjust as needed */
    width: 80px;
    margin-bottom: 20px;
}
.container{
    display: flex;
    flex-direction: column;
    align-items: center;
}


</style>
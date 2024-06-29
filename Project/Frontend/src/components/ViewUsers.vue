<template> 
<h1>All users</h1>
<div class="tabela">
        <table>
            <tr>
                <th>Name</th>
                <th>Surname</th>
                <th>Username</th>
                <th>Points</th>
                <th>Role</th>
                <th>Customer type</th>
                <th>Suspicious</th>
                <th>Blocked</th>
            </tr>
            <tr v-for="u in allUsers" :key="u.username" :class="{ selected: selectedUser && selectedUser.username === u.username }" @click="selectUser(u)">
                <td>{{u.name}}</td>
                <td>{{u.surname }} </td>
                <td>{{u.username}}</td>
                <td>{{formatPoints(u.points)}}</td>
                <td>{{formatRole(u.role)}}</td>
                <td>{{ formatCustomerType(u.customerId, u.role) }}</td>
                <td>{{formatBloked(u.suspicious)}}</td>
                <td class="td1">{{formatBloked(u.bloked)}} <button v-show = "u.role !== 'ADMINISTRATOR'" class=" btn btn-success blokButton" @click.prevent="blokUser(u.username)">block</button></td>

            </tr>
        </table>
        
    </div>
    <button class="btn btn-success press-btn" @click="showFunctions1()">View functions</button>
 <div class="function" v-if="showFunctions">

        <div class="search">
            <h3>Search</h3>
           
            <form>
                <div>
                    <label>Name: </label>
                    <input type="text" name="name" v-model="searchName" required>
                </div>
                <div>

                    <label>Surname: </label>
                   
                <input type="text" name="searchSurname"  v-model="searchSurname" required>
              
            <label>
                Username: 
                </label>
                <input type="text" name="searchUsername"  v-model="searchUsername" required>
                
            </div>
           
              
            
            <div class="search-btn-container">
            <button type="submit" class="search-btn btn-success press-btn1" @click.prevent="search()">Search</button>
          
            </div>


            </form>
           
    </div>
        <div class="sort">
    <h3>Sort</h3>
    <div class="container">
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
            <input type="radio" name="sortUsername" value="unordered" v-model="sortUsername">
            Unordered
        </label>
    </div>
    <div class="container">
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
    <div class="container">
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
    <div class="container">
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
    <div class="sortbtn-container">
        <button class="sortbtn" @click.prevent="sort()">Sort</button>
        <button class="sortbtn" @click.prevent="refresh()">Refresh</button>
    </div>
</div>

             

              <div class="filter">
        <h3>Filter</h3>
        <label for="combo1">User role:</label>
    <select id="combo1" name="combo1" v-model="roleFilter">
        <option value="all">All</option>
        <option value="administrator">Administrator</option>
        <option value="manager">Manager</option>
        <option value="worker">Worker</option>
        <option value="customer">Customer</option>
    </select><br>

    <label for="combo2">Type of customer:</label>
    <select id="combo2" name="combo2" v-model="typeFilter">
        <option value="all">All</option>
        <option value=gold>Gold</option>
        <option value=silver>Silver</option>
        <option value=bronze>Bronze</option>
        <option value=regular>Regular</option>
    </select><br>

    <button class="btn btn-success press-btn1" @click="filterChocolate()">Filter</button>

    </div>
    </div>
   
</template>

<script setup>

import axios, { all } from 'axios';
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

const copyUsers = ref([]);
const roleFilter = ref('all');
const typeFilter = ref('all');


const searchName = ref("");
const searchSurname = ref("");
const searchUsername = ref("");
const showFunctions = ref(false);




onMounted(() => {
    loadUsers();
})

function refresh()
{
    loadUsers();
}

function showFunctions1()
{
    showFunctions.value = !showFunctions.value;
}

function formatCustomerType(customerId, userRole)
{
    if(userRole === "CUSTOMER")
    {
        if(customerId === 0)
            return "gold";
        else if(customerId === 1)
            return "silver";
        else if(customerId === 2)
            return "bronze";
        else
             return "regular"; 
    }
    else
       return "";
}
function loadUsers()
{
    axios.get('http://localhost:8080/WebShopAppREST/rest/get').then(response => {
        if(response.data != "")
        {
            allUsers.value = response.data.filter(user => user.username !== usernameData.value);
            copyUsers.value = allUsers.value;
            
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


function search()
{
   axios.get("http://localhost:8080/WebShopAppREST/rest/search/?name=" + searchName.value + "&surname=" + searchSurname.value + "&username=" + searchUsername.value )
   .then(response => {  allUsers.value = response.data.filter(user => user.username !== usernameData.value); copyUsers.value = response.data.filter(user => user.username !== usernameData.value);  

    const uniqueUsers = new Set();
    this.allUsers.forEach(user1 => {
        let roleMatches = this.roleFilter === 'all' ||  user1.role.toLowerCase() === this.roleFilter;
        let typeMatches = this.typeFilter === 'all' || formatCustomerType(user1.customerId, user1.role) === this.typeFilter; 

        if (roleMatches && typeMatches) {
            uniqueUsers.add(user1);
        }
    });

    // Update the factories list
    allUsers.value = Array.from(uniqueUsers);
   });
   

   
}
function filterChocolate()
{
    allUsers.value = copyUsers.value;
    const uniqueUsers = new Set();


    this.allUsers.forEach(user1 => {
        let roleMatches = this.roleFilter === 'all' ||  user1.role.toLowerCase() === this.roleFilter;
        let typeMatches = this.typeFilter === 'all' || formatCustomerType(user1.customerId, user1.role) === this.typeFilter; 

        if (roleMatches && typeMatches) {
            uniqueUsers.add(user1);
        }
    });

    // Update the factories list
    allUsers.value = Array.from(uniqueUsers);

                 
 }







</script>

<style scoped>

template {
    height: 100%;
}

h1 {
    margin: 0 auto;
    color: #5a086a;
    padding: 0;
    background: white;
    max-width: 520px;
    margin-bottom: 20px;
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
    padding: 3px 4px; /* Adjust padding for a smaller button */
    color: white; /* Set text color */
    align-items: center;
    position: absolute; /* Position the button absolutely within the <td> */
    top: 50%; /* Adjust as needed */
    right: 5px; /* Adjust as needed */
    transform: translateY(-50%);
}

.blokButton:hover {
    background-color: #0056b3;
}

.td1 {
    position: relative; 
    width: 170px;
}

.selected {
    background-color: rgb(245, 195, 128); /* Change to your desired highlight color */
}

.sort {
    max-width: 560px;
    margin: 0 auto;
    padding: 15px;
    border: 1px solid wheat;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    background-color: wheat;
    margin-bottom: 40px;
    margin-left: 20px;
}

.sort h3 {
    font-size: 1.7em;
    margin-bottom: 10px;
    text-align: center; /* Center align the heading */
    color: #5a086a;
    
}

.sort-section {
    margin-top: 20px;
}

.container {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}

.container label {
    display: inline-block;
    width: 130px; /* Adjust width as needed */
    font-weight: bold;
    text-align: left;
}

.container label:first-child {
    margin-right: 5px; /* Add margin-right to the first label */
}

.container label:not(:first-child) {
    margin-left: 10px; /* Add margin-left to subsequent labels */
}

.container input[type="radio"] {
    margin-right: 5px;
    margin-top: 20px;
}

.sort .sortbtn-container {
    text-align: center;
    margin-top: 10px;
    display: flex;
    margin-left: 180px;
}

.sort .sortbtn {
    padding: 5px 10px;
    background-color: #5a086a;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    margin-right: 20px; /* Adjust as needed */
    width: 80px;
}

.sort .sortbtn:hover {
    background-color: #0056b3;
}


.function{
    display: flex;
    justify-content: horizontal;
    max-height: 400px;
   
}


.search {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid  wheat;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    background-color:  wheat;
    margin-bottom: 40px;
}

.search h3 {
    font-size: 1.7em;
    color: #5a086a;
}

.search .form-group {
    margin-bottom: 20px;
}

.search label {
    display: inline-block;
    width: 130px; /* Adjust width as needed */
    font-weight: bold;
    text-align: left;
}

.search input[type="text"] {
    padding: 8px;
    font-size: 1em;
    border: none;
    background: transparent;
    border-bottom: 1px solid gray;
    border-radius: 4px;
    width: calc(100% - 130px); /* Adjust width for better appearance */
}

.search-btn-container {
    text-align: center;
    margin-top: 20px;
}

.search-btn {
    padding: 5px 10px;
    background-color: #5a086a;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    text-align: center; /* Center align text */
    display: inline-block; /* Ensure inline-block display to center text properly */
}

.search-btn:hover {
    background-color: #0056b3;
}

.btn-text {
    margin-top: 5px; /* Adjust margin as needed */
    color: #555; /* Example color */
    font-size: 0.8em; /* Example font size */
}


.filter {
    max-width: 580px; /* Adjust width as needed */
    margin: 0 auto;
    padding: 20px;
    border: 1px solid wheat;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    background-color: wheat;
    margin-bottom: 40px; /* Consistent margin with other sections */
    margin-left: 20px;
}

.filter h3 {
    font-size: 1.7em;
    margin-bottom: 10px;
    text-align: center; /* Center align the heading */
    color: #5a086a;
}

.filter .form-group {
    margin-bottom: 20px;
}

.filter label {
    display: inline-block;
    width: 150px; /* Adjust width to match the input/select */
    font-weight: bold;
    text-align: left;
    margin-bottom: 10px;
}

.filter select {
    padding: 8px;
    font-size: 1em;
    border: none;
    background: transparent;
    border: 1px solid gray;
    border-radius: 4px;
    width: calc(100% - 150px); /* Adjust width for better appearance */
    margin-bottom: 10px;
}

.filter .btn-container {
    text-align: center;
    margin-top: 15px;
}

.filter .btn {
    padding: 5px 10px;
    background-color: #5a086a;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    margin-right: 10px; /* Adjust margin between buttons */
    display: inline-block;
    margin-top: 20px;
    width: 80px;
}

.filter .btn:hover {
    background-color: #0056b3;
}

.filter .form-group {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
}

.filter .form-group label {
    margin-right: 10px;
}

.filter .form-group select {
    flex: 1;
}





</style>
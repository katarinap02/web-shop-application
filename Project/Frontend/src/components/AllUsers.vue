<template>
  <h1>All users</h1>
    <div class="container">

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
            <table>
                <tr>
                    <th>Username</th>
                    <th>Name</th>
                    <th>Surname</th>
                    <th>Points</th>
                    <th>Role</th>
                </tr>
                <tr v-for="u in users" :key="u.username">
                    <td>{{ u.username }}</td>
                    <td>{{ u.name }}</td>
                    <td>{{ u.surname }}</td>
                    <td>{{ u.points }}</td>
                    <td>{{ u.role }}</td>
                </tr>
            </table>
       
    </div>

</template>
<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const users = ref([]);

const sortUsername = ref('unordered');
const sortName = ref('unordered');
const sortSurname = ref('unordered');
const sortPoints = ref('unordered');

onMounted(() => { loadUsers(); })
function loadUsers()
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/get/")
    .then(response => {  users.value = response.data; });
}

function refresh()
{
    loadUsers();
}

function sort()
{
    if(sortUsername.value === 'ascending')
        {
           
            users.value.sort((a, b) => a.username.toLowerCase().localeCompare(b.username.toLowerCase())); 
        }
        else if(sortUsername.value === 'descending')
        {
            users.value.sort((a, b) => b.username.toLowerCase().localeCompare(a.username.toLowerCase()))
        }

        if(sortSurname.value === 'ascending')
        {
           
            users.value.sort((a, b) => a.surname.toLowerCase().localeCompare(b.surname.toLowerCase())); 
        }
        else if(sortSurname.value === 'descending')
        {
            users.value.sort((a, b) => b.surname.toLowerCase().localeCompare(a.surname.toLowerCase()))
        }

        if(sortName.value === 'ascending')
        {
           
            users.value.sort((a, b) => a.name.toLowerCase().localeCompare(b.name.toLowerCase())); 
        }
        else if(sortName.value === 'descending')
        {
            users.value.sort((a, b) => b.name.toLowerCase().localeCompare(a.name.toLowerCase()))
        }

         
        if(sortPoints.value === 'ascending')
        {
           
           
            users.value.sort((a, b) => a.points - b.points); 
        }
        else if(sortPoints.value === 'descending')
        {
            users.value.sort((a, b) => b.points - a.points)
        }
}
</script>
<style scoped>
.sort {
    font-family: Arial, sans-serif;
   
    text-align: left
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

.table{
    display: flex;
    flex-direction: column;
    align-items: center;
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
}
.container{
    display: flex;
    flex-direction: column;
    align-items: center;
}

table{
    border: 1px solid black;
}
</style>
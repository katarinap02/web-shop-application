<template>
<h1>Profile</h1>
  <form name="profilForma" class="profilForma" @submit="changeUser($event)">
    
    <table>
        
<tr>
    <td class="label">Name:</td>
    <td ><input name="name" type="text" v-model="user.name"  :class = "{'error':user.name === ''}"> </td>
</tr>
<tr>
    <td class="label">Surname:</td>
    <td ><input name="surname" type="text" v-model="user.surname" :class = "{'error':user.surname === ''}"></td>
</tr>
<tr>
    <td class="label">Birthdate:</td>
    <td ><input name="birthdate" type="date" v-model="user.birthDate"></td>
</tr>
<tr>
    <td class="label">Username:</td>
    <td><p>{{user.username}}</p></td>
</tr>
<tr>
    <td class="label">Gender:</td>
    <td ><p>{{user.gender}} <button class="changeButton" @click="editRole()">edit</button></p></td>
</tr>
<tr>
    <td class="label">Role:</td>
    <td><p>{{user.role}}</p></td>
</tr>
<tr v-if="factoryName !== ''">
    <td class="label">In charge of the factory:</td>
    <td><p>{{factoryName}}</p></td>
</tr>

    </table>
    <button type="submit" class="submit">Save changes</button>
  </form>


  <p v-show = "errorMsg == 'HasError'">Field inputs are invalid.</p>
</template>

<script setup>

import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const route = useRoute();
const router = useRouter();

const errorMsg = ref("NoError");
const user = ref({username: "", password: "", name: "", surname: "", gender: "", date: "", factory: null});
const usernameData = ref(localStorage.getItem('userData'));
const factoryName = ref("");



onMounted(() => {
    loadUser();
})

function loadUser()
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/getLogedUser?username=" + usernameData.value)
    .then(response => {
        user.value = response.data;
        user.value.role = user.value.role.toLowerCase();
        user.value.gender = user.value.gender.toLowerCase();
        console.log(response.data.factory.name)
        if(response.data.factory != null)
        {
            factoryName.value = response.data.factory.name;
        }
        
        
    })
    .catch(error => {
      
    });
}

function isAdministrator() {
      return this.user.role.toLowerCase() === 'administrator';
    }

function editRole()
{
    if(this.user.gender === 'female' || this.user.gender === 'FEMALE')
    {
        this.user.gender = 'male';
    }
    else {
        this.user.gender = 'female';
    }
    
}

function changeUser(event)
{
    event.preventDefault();

     if(!this.user.name || !this.user.surname)
{
   
    errorMsg.value = "HasError";
}
else {
    user.value.gender = user.value.gender.toUpperCase();
    user.value.role = user.value.role.toUpperCase();
    axios.post("http://localhost:8080/WebShopAppREST/rest/editUser", this.user)
    .then(response => {
        loadUser();
        })
    .catch(error => {
        console.error('Error:', error);
    });
}

}




    
</script>


<style scoped>

*{
    padding: 0;
    margin: 0;
}

.profilForma{
    margin: 0 auto;
    max-width: 480px;
    background: white;
    text-align: left;
    border-radius: 10px;
    padding: 25px;
}

h1 {
    text-align: center;
    margin-top: 20px;
    color: #5a086a;
}

.profilForma .label {
    color: gray;
    display: inline-block;
    margin: 20px 0 15px;
    font-size: 0.95em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;

}

.profilForma input {
    padding: 5px 10px;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #5a086a;
    color: #555;
    display: block;
    width: 320px;
    margin-left: 10px;
}

.profilForma  p {
    padding: 5px 10px;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #aeaeae;
    color: #787878;
    display: block;
    width: 320px;
    margin-left: 10px;
}

.profilForma .error{
    border-bottom: 2px solid red;
}

.profilForma button {
    padding: 0.5rem;
    border: none;
    border-radius: 10px;
    background-color: #5a086a;
    color: white;
    cursor: pointer;
    width: 100%;
    height: 40px;
    font-size: 0.8em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;
    align-content: center;
    margin-top: 20px;
}

.profilForma button:hover {
    background-color: #0056b3;
}

.profilForma .changeButton {
    width: 40px;
    height: 20px;
    font-weight: 100;
    font-size: 0.75em;
    text-transform: lowercase;
    padding: 0;
    margin-left: 205px;
    background-color:gray ;
}

.profilForma .reg_btn
{
    height: 30px;
    margin: 5px;
}
a {
    vertical-align: center;
}


</style>
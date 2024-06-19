<template>
<h1>Create factory</h1>
  <form name="fabrikaForma" @submit="addFactory($event)" class="fabrikaForma">
    
    <table>
        <tr>
    <td class="label">Name:</td>
    <td><input name="name" type="text" v-model="factory.name" :class = "{'error':factoryValid.name === ''}"></td>
</tr>
<tr>
    <td class="label">Latitude:</td>
    <td><input name="latitude" type="text" v-model="factory.location.latitude" ></td>
</tr>
<tr>
    <td class="label">Longitude:</td>
    <td><input name="longitude" type="text" v-model="factory.location.longitude" ></td>
</tr>
<tr>
    <td class="label">Address:</td>
    <td><input name="address" type="text" v-model="factory.location.address" :class = "{'error':factoryValid.location.address === ''}"></td>
</tr>
<tr>
    <td class="label">Image:</td>
    <td><input name="image" type="text" v-model="factory.logoUrl" :class = "{'error':factoryValid.logoUrl === ''}"></td>
</tr>
<tr>
    <td class="label">Start Hour:</td>
    <td><input name="start" type="time" v-model="factory.workingHours.startHour"></td>
</tr>
<tr>
    <td class="label">End Hour:</td>
    <td><input name="end" type="time" v-model="factory.workingHours.endHour"></td>
</tr>
<tr>
    <td class="label">Maganer</td>
    <td v-if="managers">
        <select name="manager" id="manager" v-model="mainManager">
            <option v-for="manager in managers" :key="manager.username" :value="manager">
                {{ manager.name }} {{ manager.surname }}
            </option>
        </select>
    </td>
    
</tr>


<tr>
    <td></td>
    
</tr>

    </table>
    <button type="submit" class="submit">Create</button>
 
 
  </form>
  <p v-show = "errorMsg == 'HasError'">Field inputs are invalid.</p>
</template>
<script setup>

import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const route = useRoute();
const router = useRouter();
const factory = ref({ name: "",workingHours: {
    startHour: "08:00",
    endHour: "15:00"
  }, isWorking: false, location: {
    latitude: 0,
    longitude: 0,
    address: ""
  }, logoUrl: "", rate: 0});
const factoryValid = ref({ name: "a", isWorking: false,  location: {
    latitude: 0,
    longitude: 0,
    address: "a"
  }, logoUrl: "a", rate: -1.0});
const errorMsg = ref("NoError");

const managers = ref('');
const mainManager = ref(null);
const factoryReturn = ref('');
const managerFactory = ref({factoryId: -1, managerUsername: ''})

onMounted(() => {
    loadManagers();
})

function loadManagers()
{
    axios.get('http://localhost:8080/WebShopAppREST/rest/getManagers').then(response => {
        if(response.data != "" && response.data.length > 0)
        {
            managers.value = response.data;
            mainManager.value = managers.value[0];
           
        }
        else {
            managers.value = '';
        }
    })
}


function addFactory(event)
{
    event.preventDefault();
    
    
    this.factoryValid = factory.value;
  
    
   if(!this.factoryValid.name || !this.factoryValid.logoUrl || !this.factoryValid.location.address)
{
   
    errorMsg.value = "HasError";
}
else if(this.managers.length == 0)
{
    axios.post("http://localhost:8080/WebShopAppREST/rest/factories/add", this.factory)
    .then(response => {
        console.log(response.data);
        this.factoryReturn = response.data;
        router.push({ path: '/register', query: { id: this.factoryReturn.id } });
        })
    .catch(error => {
        console.error('Error:', error);
    });
}
else
{
    
    axios.post("http://localhost:8080/WebShopAppREST/rest/factories/add", this.factory)
    .then(response => {
        console.log(response.data);
        this.factoryReturn = response.data; // Ensure this.factoryReturn is assigned properly

        // Update managerFactory with id and username
        this.managerFactory.factoryId = this.factoryReturn.id;
        this.managerFactory.managerUsername = this.mainManager.username;
        console.log(this.managerFactory);
        if(this.managers.length > 0)
        // Make the second axios post request within this then block to ensure it waits for the first one to complete
        return axios.post("http://localhost:8080/WebShopAppREST/rest/editManager", this.managerFactory);
    })
    .then(response => {
        console.log(response.data);
        router.push("/");
    })
    .catch(error => {
        console.error('Error:', error);
    });
    
    

    

    

}
    
   
}
</script>

<style scoped>

.fabrikaForma {
    margin: 0 auto;
    max-width: 420px;
    background: white;
    text-align: left;
    border-radius: 10px;
    padding: 30px;
}

h1 {
    text-align: center;
    margin-top: 10px;
    color: #5a086a;
}

.fabrikaForma .label {
    color: #aaa;
    display: inline-block;
    margin: 22px 0 15px;
    font-size: 0.9em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;

}

.fabrikaForma input {
    padding: 2px 6px;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #ddd;
    color: #555;
    display: block;
    width: 100%;
}

.error{
    border: 2px solid red;
}

.fabrikaForma button {
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
}

.fabrikaForma button:hover {
    background-color: #0056b3;
}

.fabrikaForma .reg_btn
{
    height: 30px;
    margin: 5px;
}
</style>
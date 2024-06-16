<template>
  <form name="fabrikaForma" @submit="addFactory($event)" class="fabrikaForma">
    <table>
        <tr>
    <td>Name:</td>
    <td><input name="name" type="text" v-model="factory.name" :class = "{'error':factoryValid.name === ''}"></td>
</tr>
<tr>
    <td>Latitude:</td>
    <td><input name="latitude" type="text" v-model="factory.location.latitude" ></td>
</tr>
<tr>
    <td>Longitude:</td>
    <td><input name="longitude" type="text" v-model="factory.location.longitude" ></td>
</tr>
<tr>
    <td>Address:</td>
    <td><input name="address" type="text" v-model="factory.location.address" :class = "{'error':factoryValid.location.address === ''}"></td>
</tr>
<tr>
    <td>Image:</td>
    <td><input name="image" type="text" v-model="factory.logoUrl" :class = "{'error':factoryValid.logoUrl === ''}"></td>
</tr>
<tr>
    <td>Start Hour:</td>
    <td><input name="start" type="time" v-model="factory.workingHours.startHour"></td>
</tr>
<tr>
    <td>End Hour:</td>
    <td><input name="end" type="time" v-model="factory.workingHours.endHour"></td>
</tr>


<tr>
    <td></td>
    <td><button type="submit">Add</button></td>
</tr>

    </table>
 
 
  </form>
  <p v-show = "errorMsg == 'HasError'">Field inputs are invalid.</p>
</template>
<script setup>

import axios from 'axios';
import { ref } from 'vue';
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


function addFactory(event)
{
    event.preventDefault();
    
    
    this.factoryValid = factory.value;
  
    
   if(!this.factoryValid.name || !this.factoryValid.logoUrl || !this.factoryValid.location.address)
{
   
    errorMsg.value = "HasError";
}
else
{
    
    axios.post("http://localhost:8080/WebShopAppREST/rest/factories/add", this.factory, this.location, this.workingHours)
    .then(response => {console.log(response.data); 
       
    });

    router.push("/");

    

}
    
   
}
</script>

<style scoped>

.fabrikaForma{
    display: flex;
    flex-direction: column;
    align-items: center;
}

.error{
    border: 2px solid red;
}
</style>
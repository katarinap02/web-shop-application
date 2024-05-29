<template>
  <form name="cokoladaForma" @submit="addChocolate($event)">
    <table>
        <tr>
    <td>Name:</td>
    <td><input name="name" type="text" v-model="chocolate.name" :class = "{'error':chocolateValid.name === ''}"></td>
</tr>
<tr>
    <td>Price:</td>
    <td><input name="price" type="text" v-model="chocolate.price" :class = "{'error':chocolateValid.price <= 0}"></td>
</tr>
<tr>
    <td>Type:</td>
    <td><input name="kind" type="text" v-model="chocolate.kind" :class = "{'error':chocolateValid.kind === ''}"></td>
</tr>
<tr>
    <td>Category:</td>
    <td><input name="type" type="text" v-model="chocolate.type" :class = "{'error':chocolateValid.type === ''}"></td>
</tr>
<tr>
    <td>Weight:</td>
    <td><input name="grams" type="text" v-model="chocolate.grams" :class = "{'error':chocolateValid.grams <= 0}"></td>
</tr>
<tr>
    <td>Description:</td>
    <td><input name="text" type="text" v-model="chocolate.description" :class = "{'error':chocolateValid.description === ''}"></td>
</tr>
<tr>
    <td>Image:</td>
    <td><input name="image" type="text" v-model="chocolate.imageUrl" :class = "{'error':chocolateValid.imageUrl === ''}"></td>
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
const factoryId = ref(route.params.id);
const chocolate = ref({ name: "", price: 0, kind: "", factory: -1, type: "", grams: 0, description: "", imageUrl: "", status: false, number: 0 });
const chocolateValid = ref({ name: "a", price: 1, kind: "a", factory: 0, type: "a", grams: 1, description: "a", imageUrl: "a", status: false, number: 0 });
const errorMsg = ref("NoError");

function hasEmptyFields(object)
{
    const isEmpty = value => value === "" || value === null || value === undefined;

    return Object.values(object).some(isEmpty);
}
function addChocolate(event)
{
    event.preventDefault();
    
    
    this.chocolate.factory = factoryId.value;
    this.chocolateValid = chocolate.value;


    if(!this.chocolateValid.name || this.chocolateValid.price <= 0 || !this.chocolateValid.kind 
    || !this.chocolateValid.factory <= 0 || !this.chocolateValid.type || !this.chocolateValid.grams <= 0
|| !this.chocolateValid.description || !this.chocolateValid.imageUrl)
{
    errorMsg.value = "HasError";
}
else
{
    
    axios.post("http://localhost:8080/WebShopAppREST/rest/chocolates/", this.chocolate)
    .then(response => {alert("Uslo"); console.log(response.data); 
       
    });

     this.router.push({name: 'ShowFactory', params: { id: factoryId.value } });

    

}
    
   
}
</script>

<style scoped>

.error{
    border: 2px solid red;
}
</style>


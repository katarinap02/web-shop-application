<template>
<h1>Add Chocolate</h1>
  <form name="cokoladaForma" @submit="addChocolate($event)" class="cokoladaForma">
    <table>
        <tr>
    <td class="label">Name:</td>
    <td><input name="name" type="text" v-model="chocolate.name" :class = "{'error':chocolateValid.name === ''}"></td>
</tr>
<tr>
    <td class="label">Price:</td>
    <td><input name="price" type="number" v-model="chocolate.price" :class = "{'error':chocolateValid.price <= 0}"></td>
</tr>
<tr>
    <td class="label">Type:</td>
    <td><input name="kind" type="text" v-model="chocolate.kind" :class = "{'error':chocolateValid.kind === ''}"></td>
</tr>
<tr>
    <td class="label">Category:</td>
    <td><input name="type" type="text" v-model="chocolate.type" :class = "{'error':chocolateValid.type === ''}"></td>
</tr>
<tr>
    <td class="label">Weight:</td>
    <td><input name="grams" type="number" v-model="chocolate.grams" :class = "{'error':chocolateValid.grams <= 0}"></td>
</tr>
<tr>
    <td class="label">Description:</td>
    <td><input name="text" type="text" v-model="chocolate.description" :class = "{'error':chocolateValid.description === ''}"></td>
</tr>
<tr>
    <td class="label">Image:</td>
    <td><input name="image" type="text" v-model="chocolate.imageUrl" :class = "{'error':chocolateValid.imageUrl === ''}"></td>
</tr>
    </table>
    <button type="submit">Add</button>
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


function addChocolate(event)
{
    event.preventDefault();
    
    
    this.chocolate.factory = factoryId.value;
    this.chocolateValid = chocolate.value;
  
    
   if(!this.chocolateValid.name || this.chocolateValid.price < 0 || !this.chocolateValid.kind 
    || this.chocolateValid.factory < 0 || !this.chocolateValid.type || this.chocolateValid.grams < 0
|| !this.chocolateValid.description || !this.chocolateValid.imageUrl)
{
   
    errorMsg.value = "HasError";
}
else
{
    
    axios.post("http://localhost:8080/WebShopAppREST/rest/chocolates/", this.chocolate)
    .then(response => {console.log(response.data); 
       
    });

     this.router.push({name: 'ShowFactory', params: { id: factoryId.value } });

    

}
    
   
}
</script>

<style scoped>

.cokoladaForma {
    margin: 0 auto;
    max-width: 480px;
    background: white;
    text-align: left;
    border-radius: 10px;
    padding: 25px;
}

h1 {
    margin: 0 auto;
    color: #5a086a;
    padding: 0;
    background: white;
    max-width: 520px;
}

.cokoladaForma .label {
    color: gray;
    display: inline-block;
    margin: 20px 0 15px;
    font-size: 0.95em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;

}

.cokoladaForma input {
    padding: 2px 6px;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #aeaeae;
    color: #555;
    display: block;
    width: 290px;
}

.cokoladaForma .error{
    border-bottom: 2px solid red;
}

.cokoladaForma button {
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
    margin-top: 5px;
}

.cokoladaForma button:hover {
    background-color: #0056b3;
}
</style>


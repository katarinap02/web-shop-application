<template>
    <form name="cokoladaForma" @submit="updateChocolate($event)">
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
    <td>Quantity:</td>
    <td><input name="number" type="text" v-model="chocolate.number" :class = "{'error':chocolateValid.number < 0}" ></td>
</tr>
<tr>
    <td></td>
    <td><button type="submit">Update</button></td>
</tr>

          
      </table>
      
  
    </form>
    <p v-show = "errorMsg == 'HasError'">Field inputs are invalid.</p>
  </template>
  <script setup>
  
  import axios from 'axios';
  import { onMounted, ref } from 'vue';
  import { useRouter, useRoute } from 'vue-router';
  
  const route = useRoute();
  const router = useRouter();
  const chocolateId = ref(route.params.id);
  const chocolate = ref({ name: "", price: 0, kind: "", factory: -1, type: "", grams: 0, description: "", imageUrl: "", status: false, number: 0 });
  const chocolateValid = ref({ name: "a", price: 1, kind: "a", factory: 0, type: "a", grams: 1, description: "a", imageUrl: "a", status: false, number: 0 });
const errorMsg = ref("NoError");
  onMounted(() => { getByChocolateId(chocolateId); });

  
  function getByChocolateId(chocolateId)
  {
   
     axios.get("http://localhost:8080/WebShopAppREST/rest/chocolates/getId/" + chocolateId.value)
     .then(response => { chocolate.value = response.data; console.log(response.data)});
  }
  function updateChocolate(event)
  {
      event.preventDefault();    
      this.chocolateValid = chocolate.value;
      if(!this.chocolateValid.name || this.chocolateValid.price <= 0 || !this.chocolateValid.kind 
    || !this.chocolateValid.factory <= 0 || !this.chocolateValid.type || !this.chocolateValid.grams <= 0
|| !this.chocolateValid.description || !this.chocolateValid.imageUrl || this.chocolateValid.number < 0)
{
    errorMsg.value = "HasError";
}
else
{
    axios.post("http://localhost:8080/WebShopAppREST/rest/chocolates/" + chocolateId.value, this.chocolate)
      .then(response => { console.log(response.data);
      this.router.push({name: 'ShowFactory', params: { id: chocolate.value.factory } });
       
       });

}
      
  }
  </script>
  <style scoped>

  .error{
      border: 2px solid red;
  }
  </style>
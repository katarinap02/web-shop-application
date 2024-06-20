<template>


    <h1>{{ chocolate.name }}</h1>
    <img :src="chocolate.imageUrl" alt="Factory Logo" style="width: 200px; height: 200px;">
   
    <div>
        <table>
           
            <tr>
                <td>Price: </td>
                <td>{{ chocolate.price }}</td>
            </tr>
            <tr>
                <td>Type: </td>
                <td >{{ chocolate.kind }}</td>
            </tr>

            <tr>
                <td>Category: </td>
                <td >{{ chocolate.type }}</td>
            </tr>

            <tr>
                <td>Weight: </td>
                <td >{{ chocolate.grams }}</td>
            </tr>

            <tr>
                <td>Description: </td>
                <td >{{ chocolate.description }}</td>
            </tr>
            <tr>
                <td>Amount: </td>
                <td >{{ chocolate.number }}</td>
            </tr>
            <tr>
                <td>In stock: </td>
                <td >{{ chocolate.status }}</td>
            </tr>
        </table>

        <form><tr>
            <td>Select amount:</td>
            <td><input type='number' name="chocolateAmount" v-model="chocolateAmount"></td>
            <td><button type='submit' v-on:click="addToCart()">Save</button></td>
        </tr></form>
    </div>
    <br>

</template>
<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const route = useRoute();
const router = useRouter();
const chocolateId = ref(route.params.id);
const cartId = ref(route.params.cartId);
const chocolate = ref({ name: "", price: 0, kind: "", factory: -1, type: "", grams: 0, description: "", imageUrl: "", status: false, number: 0 });
const chocolateAmount = ref(0);

onMounted(() => {  getChocolateId(chocolateId); })


function getChocolateId(chocolateId)
  {
   
     axios.get("http://localhost:8080/WebShopAppREST/rest/chocolates/getId/" + chocolateId.value)
     .then(response => { chocolate.value = response.data; console.log(response.data)});
  }

  function addToCart()
  {
    if(chocolateAmount.value > chocolate.value.number)
    {
        alert("There is not enough chocolates");
    }
    else
    {
                
        chocolate.value.number = chocolate.value.number - chocolateAmount.value;
        axios.post("http://localhost:8080/WebShopAppREST/rest/chocolates/" + chocolateId.value, this.chocolate)
      .then(response => { console.log(response.data);  });

        axios.post("http://localhost:8080/WebShopAppREST/rest/carts/addtocart/?cartId=" + cartId.value + "&chocolateId=" + chocolateId.value + "&amount=" + chocolateAmount.value + "&price=" + chocolate.value.price + "&factoryId=" + chocolate.value.factory)
        .then(response => { alert("Success!"); this.router.push({name: 'ShowFactory', params: {id: chocolate.value.factory }}); });
      
    }
  }

function updateChocolateAmount()
{
    axios.post("http://localhost:8080/WebShopAppREST/rest/chocolates/" + chocolateId.value, this.chocolate)
      .then(response => { console.log(response.data);
      this.router.push({name: 'ShowFactory', params: { id: chocolate.value.factory } });
       
       });
}
</script>
<style scoped>

div {
    display: flex;
    flex-direction: column;
    align-items: center;
}


</style>
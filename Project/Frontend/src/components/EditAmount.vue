<template>
 <h1>{{ chocolate.name }}</h1>
    
 <img :src="chocolate.imageUrl" alt="Factory Logo" style="width: 300px; height: 300px;">
    <div class="chocolate-info">
       
        <table class="info">
           
            <tr>
                <td>Price: </td>
                <td><p>{{ chocolate.price }}</p></td>
            </tr>
            <tr>
                <td>Type: </td>
                <td ><p>{{ chocolate.kind }}</p></td>
            </tr>

            <tr>
                <td>Category: </td>
                <td ><p>{{ chocolate.type }}</p></td>
            </tr>

            <tr>
                <td>Weight: </td>
                <td ><p>{{ chocolate.grams }}</p></td>
            </tr>

            <tr>
                <td>Description: </td>
                <td ><p>>{{ chocolate.description }}</p></td>
            </tr>
            <tr>
                <td>Amount: </td>
                <td ><p>{{ chocolate.number }}</p></td>
            </tr>
            <tr>
                <td>In stock: </td>
                <td ><p>{{ chocolate.status }}</p></td>
            </tr>
        </table>

        <form class="select-amount"><tr>
            <td>Select amount:</td>
            <td><input type='number' name="chocolateAmount" v-model="chocolateAmount"></td>
            <td><button type='submit' class="btn btn-success save-btn" v-on:click="updateAmount()">Save</button></td>
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
const chocolateAmount = ref(route.params.amount);
const oldAmount = chocolateAmount.value;
onMounted(() => {  getChocolateId(chocolateId); })


function getChocolateId(chocolateId)
  {
   
     axios.get("http://localhost:8080/WebShopAppREST/rest/chocolates/getId/" + chocolateId.value)
     .then(response => { chocolate.value = response.data; console.log(response.data)});
  }


  function updateAmount()
  {
    
    alert(oldAmount);
    if(chocolateAmount.value > chocolate.value.number)
    {
        alert("There is not enough chocolates");
    }
    else if(chocolateAmount.value < 1)
    {
        alert("You need to enter at least 1.");
    }
    else
    {
        var difference = chocolateAmount.value - oldAmount;
       
        chocolate.value.number = chocolate.value.number - difference;
        axios.post("http://localhost:8080/WebShopAppREST/rest/chocolates/" + chocolateId.value, this.chocolate)
      .then(response => { console.log(response.data); 

        axios.post("http://localhost:8080/WebShopAppREST/rest/carts/updateAmount/?cartId=" + cartId.value + "&chocolateId=" + chocolateId.value + "&amount=" + chocolateAmount.value + "&price=" + chocolate.value.price)
        .then(response => { });
       
       });
       this.router.push({name: "ShowCart", params: {cartid: cartId.value}});
     
     
      
      
    }
  }

</script>
<style scoped>
.chocolate-info {
    display: inline-block;
    vertical-align: middle; 
    margin-right: 20px; }
  
  h1 {
    
      color: #5a086a;
      margin-top: 20px;
      
   
  }
  
  
  
  .chocolate-info img {
   
    vertical-align: middle;
  }
  
  .info{

      margin: 0 auto;
      max-width: 480px;
      background: white;
      text-align: left;
      border-radius: 10px;
      padding: 25px;
  }
  
  .info .label {
      color: gray;
      display: inline-block;
      margin: 20px 0 15px;
      font-size: 0.95em;
      text-transform: uppercase;
      letter-spacing: 1px;
      font-weight: bold;
  
  }
  
  .info  p {
      padding: 5px 10px;
      box-sizing: border-box;
      border: none;
      border-bottom: 1px solid #aeaeae;
      color: #787878;
      display: block;
      width: 320px;
      margin-left: 10px;
  }
  
  .select-amount{
    margin-top: 20px;
    display: flex;
    flex-direction: row;
    
  }

  .select-amount td{
    color: #5a086a;
    font-size: x-large;
    font-weight: bold;
  }

  .save-btn{
    background-color: #5a086a;
    border: #5a086a;
}
.btn:hover {
    background-color: #0056b3;
}

.select-amount input {
    padding: 3px 6px;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #aeaeae;
    color: #555;
    width: 100%;
}
</style>
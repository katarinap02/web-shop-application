<template>


    
    
    <h1>{{ chocolate.name }}</h1>
    <img :src="chocolate.imageUrl" alt="Factory Logo" style="width: 300px; height: 300px;">
    <div class = "chocolate-info">
 
       
        <table class = "info">
           
            <tr>
                <td class="label">Price: </td>
                <td><p>{{ price }}</p></td>
            </tr>
            <tr>
                <td class="label">Type: </td>
                <td ><p>{{ chocolate.kind }}</p></td>
            </tr>

            <tr>
                <td class="label">Category: </td>
                <td ><p>{{ chocolate.type }}</p></td>
            </tr>

            <tr>
                <td class="label">Weight: </td>
                <td ><p>{{ chocolate.grams }}</p></td>
            </tr>

            <tr>
                <td class="label">Description: </td>
                <td ><p>{{ chocolate.description }}</p></td>
            </tr>
            <tr>
                <td class="label">Amount: </td>
                <td ><p>{{ chocolate.number }}</p></td>
            </tr>
            <tr>
                <td class="label">In stock: </td>
                <td ><p>{{ chocolate.status }}</p></td>
            </tr>
        </table>

        <form class="select-amount"><tr>
            <td>Select amount:</td>
            <td><input type='number' name="chocolateAmount" v-model="chocolateAmount"></td>
            <td><button type='submit' class="btn btn-success save-btn" v-on:click="addToCart()">Save</button></td>
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
const discount = ref(0);
const user = ref('');
const usernameData = ref(localStorage.getItem('userData'));
const price = ref(0);

onMounted(() => {  getChocolateId(chocolateId); })


function getChocolateId(chocolateId)
  {
   
     axios.get("http://localhost:8080/WebShopAppREST/rest/chocolates/getId/" + chocolateId.value)
     .then(response => { chocolate.value = response.data; console.log(response.data)

        loadUser();
     });
  }

  function loadUser(){
    axios.get("http://localhost:8080/WebShopAppREST/rest/getLogedUser?username=" + usernameData.value)
    .then(response => {
        user.value = response.data;
        loadDiscount(user.value.username);
    })
    .catch(error => {
      //  localStorage.setItem('userData', JSON.stringify(""));
    });

}


function loadDiscount(username)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/getdiscount/" + username)
    .then(response => { discount.value = response.data;  price.value = chocolate.value.price * discount.value; })
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
      .then(response => { console.log(response.data);  

        axios.post("http://localhost:8080/WebShopAppREST/rest/carts/addtocart/?cartId=" + cartId.value + "&chocolateId=" + chocolateId.value + "&amount=" + chocolateAmount.value + "&price=" + price.value + "&factoryId=" + chocolate.value.factory)
        .then(response => { alert("Success!");     this.router.push({name: 'ShowFactory', params: {id: chocolate.value.factory }}); });
      
      
      });

        
      
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
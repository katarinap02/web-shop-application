<template>

  <div>

    <h1>Current cart</h1>
 
  <table class="tabela" id="tabelacokolada">
            <tr>
            <th class="red">Name</th>
            <th class="red">Price</th>
            <th class="red">Type</th>
            <th class="red">Category</th>
            <th class="red">Weight</th>
            <th class="red">Description</th>
            <th class="red">Image</th>
            <th class="red">Amount</th>
           
        </tr>


            <tr v-for="c in items" :key="c.id" :class="{ selected: selectedChocolate && selectedChocolate.id === c.id }" @click="selectChocolate(c)" >
                <td class="red">{{ c.name }}</td>
                <td class="red">{{ c.price }}</td>
                <td class="red">{{ c.kind }}</td>
                <td class="red">{{ c.type }}</td>
                <td class="red">{{ c.grams }}</td>
                <td class="red">{{ c.description }}</td>
               
                <td>
            <img :src="c.imageUrl" alt="ChocolateImage" style="width: 50px; height: 50px;">
          </td>
          <td class="red">{{ getAmount(c.id) }}</td>
               <td><button v-on:click="editAmount(c.id)">Edit amount</button></td>
             
            </tr>
        </table>

        <table>
          <td>Price: </td>
          <td>{{ shoppingCart.price }}</td>
        </table>

        <button  class="delete" @click="deleteSelectedChocolate()">Delete selected</button>

        

 

  </div>

  

</template>
<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();

const items = ref([]);
const selectedChocolate = ref(null);
const shoppingCart = ref({
    "chocolateIds": [
        -1
    ],
    "customerName": "",
    "id": 0,
    "price": 0.0,
    "isOpened": false,
    "factoryId": -1
});

const user = ref('');

onMounted(() => {
    loadUser();

})

function loadUser(){
    user.value = JSON.parse(localStorage.getItem('userData'));

}


onMounted(() => {loadCart(); loadItems();})

function loadCart()
{
   axios.get("http://localhost:8080/WebShopAppREST/rest/carts/" + user.value.username)
   .then(response => {shoppingCart.value = response.data; console.log(response.data);});

}

function loadItems()
{
  axios.get("http://localhost:8080/WebShopAppREST/rest/carts/getItems/" + shoppingCart.value.id)
  .then(response => { items.value = response.data; console.log(response.data);})
}

function selectChocolate(chocolate)
{
    this.selectedChocolate = chocolate;
    console.log(this.selectedChocolate);
}
function deleteSelectedChocolate()
{
    if(this.selectedChocolate != null)
    {
      var amount = this.shoppingCart.chocolateIds.filter(id => id === this.selectedChocolate.id).length;
        axios.delete("http://localhost:8080/WebShopAppREST/rest/carts/removeChocolate/?cartId=" + shoppingCart.value.id + "&chocolateId=" + this.selectedChocolate.id)
        .then(response => { shoppingCart.value = response.data; loadItems();})

        this.selectChocolate.amount = this.selectChocolate.amount + amount;
        axios.post("http://localhost:8080/WebShopAppREST/rest/chocolates/" + this.selectedChocolate.id, this.selectedChocolate)
      .then(response => { console.log(response.data);  });
        
    }
}

function editAmount(chocolateId)
{
  var amount = this.shoppingCart.chocolateIds.filter(id => id === chocolateId).length;
  this.router.push({name: 'EditAmount', params: {id: chocolateId, cartId: shoppingCart.value.id, amount: amount}}); 
}
function getAmount(chocolateId)
{
  return this.shoppingCart.chocolateIds.filter(id => id === chocolateId).length;
  
}
</script> 
<style scoped>


table{
  border: 1px solid black;

}

 div {
  display: flex;
  flex-direction: column;
  align-items: center;
 }

 #tabelacokolada{
    border: 1px solid black;
}

.red{
    border: 1px solid black;
}

.selected {
    background-color: grey; /* Change to your desired highlight color */
}
</style>
<template>
    <div>
        <h1>Order details</h1>
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
            <th></th>
           
        </tr>


            <tr v-for="c in items" :key="c.id" >
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
              
             
            </tr>
        </table>

        <table class="price">
          <td>Price: </td>
          <td>{{ price }}</td>
        </table>
    </div>

</template>
<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const orderId =  ref(route.params.orderId);
const items = ref([]);
const price = ref(0);
const chocolateIds = ref([]);

onMounted(() => {
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/getitems/" + orderId.value)
    .then(response => { items.value = response.data; 
        
        axios.get("http://localhost:8080/WebShopAppREST/rest/buys/getids/" + orderId.value)
        .then(response => { chocolateIds.value = response.data;

            axios.get("http://localhost:8080/WebShopAppREST/rest/buys/getprice/" + orderId.value)
            .then(response => { price.value = response.data; })
        }

        );

    });

});
function getAmount(chocolateId)
{
  return chocolateIds.value.filter(id => id === chocolateId).length;
  
}
</script>

<style scoped>

div {
    display: flex;
    flex-direction: column;
    align-items: center;
   }
  
   .price {
    color: #5a086a;
    font-weight: bold;
    font-size: x-large;
  
   }
  
  .tabela {
      color: #381d11;
      background-color: wheat;
      border-collapse: collapse;
      margin-bottom: 10px;
      
      
  }
  
  .tabela th{
    padding: 8px;
  text-align: center;
  border-bottom: 1px solid #ddd;
    font-size: larger;
    background-color: #5a086a;
    color: antiquewhite;
    text-align: center;
    
  }
  .tabela td {
  padding: 8px;
  text-align: center;
  border-bottom: 1px solid #ddd;
    font-size: larger;
  }
  
  .tabela .create {
    text-decoration: none;
    background-color: #5a086a;
    color: antiquewhite;
    margin-left: auto; 
  }
  
  
  
  .tabela tr:hover {
  background-color: beige;
  }
  
  .tabela tr:hover {
  background-color: beige;
  }
  
  h1 {
    margin-top: 20px;
   
    vertical-align: middle;
  
      color: #5a086a;
   
    background: white;
    margin-bottom: 10px;
  }
  
  .title {
    flex-grow: 1; 
    text-align: center;
  }
  
</style>
<template>
    <form name="cokoladaForma" @submit="updateChocolate($event)">
      <table>
          <tr>
              <td>Naziv:</td>
              <td><input name="name" type="text" v-model="chocolate.name"></td>
          </tr>
          <tr>
              <td>Cena:</td>
              <td><input name="price" type="text" v-model="chocolate.price"></td>
          </tr>
          <tr>
              <td>Vrsta:</td>
              <td><input name="kind" type="text" v-model="chocolate.kind" ></td>
          </tr>
          <tr>
              <td>Tip:</td>
              <td><input name="type" type="text" v-model="chocolate.type"></td>
          </tr>
          <tr>
              <td>Gramaza:</td>
              <td><input name="grams" type="text" v-model="chocolate.grams"></td>
          </tr>
          <tr>
              <td>Opis:</td>
              <td><input name="text" type="text" v-model="chocolate.description"></td>
          </tr>
          <tr>
              <td>Slika:</td>
              <td><input name="image" type="text" v-model="chocolate.imageUrl"></td>
          </tr>
          <tr>
            <td>Kolicina:</td>
            <td><input name="number" type="text" v-model="chocolate.number"></td>
        </tr>
          <tr>
              <td>    </td>
              <td><button type="submit">Izmeni</button></td>
          </tr>
          
      </table>
      
  
    </form>
  </template>
  <script setup>
  
  import axios from 'axios';
  import { onMounted, ref } from 'vue';
  import { useRouter, useRoute } from 'vue-router';
  
  const route = useRoute();
  const chocolateId = ref(route.params.id);
  const chocolate = ref({ name: "", price: 0, kind: "", factory: -1, type: "", grams: 0, description: "", imageUrl: "", status: false, number: 0 });
  onMounted(() => { getByChocolateId(chocolateId); });

  
  function getByChocolateId(chocolateId)
  {
   
     axios.get("http://localhost:8080/WebShopAppREST/rest/chocolates/getId/" + chocolateId.value)
     .then(response => { chocolate.value = response.data; console.log(response.data)});
  }
  function updateChocolate(event)
  {
      event.preventDefault();    
     
      axios.post("http://localhost:8080/WebShopAppREST/rest/chocolates/" + chocolateId.value, this.chocolate)
      .then(response => { console.log(response.data)});
  }
  </script>
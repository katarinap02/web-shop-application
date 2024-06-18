<template>
    <div>
        <h1>
     {{ !defaultUser ? 'Register' : (defaultUser.role === 'ADMINISTRATOR' ? 'Create Manager' : (defaultUser.role === 'MANAGER' ? 'Create Worker' : '')) }}
        </h1>
        <form class = "form" v-on:submit="RegisterNew($event)">
            <div>
                <label for="username">Username:</label>
                <input type="text" id = "username" v-model="user.username" required>
            </div>
            <div>
                <label for="password1">Password:</label>
                <input type="text" id = "password1" v-model="password1" required>
            </div>
            <div>
                <label for="password2">Password:</label>
                <input type="text" id = "password2" v-model="password2" required>
            </div>
            <div>
                <label for="name">Firstname:</label>
                <input type="text" id="name"  v-model="user.name" required>
            </div>
            <div>
                <label for="surname">Surname:</label>
                <input type="text" id="surname"  v-model="user.surname" required>
            </div>
            <div>
                <label for="gender">Gender:</label>
                <input type="radio" id="male" name="gender" value="male" v-model="male" @click="addGenderMale()">
                <label for="male" id="malelabel">Male</label>
                <input type="radio" id="female" name="gender" value="female" v-model="female" @click="addGenderFemale()">
                <label for="female">Female</label>
            </div>

            <div>
                <label for="birthdate">Birthdate:</label>
                <input type="date" id="birthdate" v-model="user.date">
            </div>
            <button type="submit" class="submit">{{ !defaultUser ? 'Register' : defaultUser.role === 'ADMINISTRATOR' || 'MANAGER' ? 'Create ' : '' }}</button>
        </form>
        <table>
       
    </table>
    <p>{{ error }}</p>
    </div>
  
</template>

<script setup>

import axios from "axios";
import { onMounted, ref } from "vue";
import { useRouter, useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();
onMounted(() => {
    loadBooks();
})


const male = ref(false);
const female = ref(true);

const password1 = ref('');
const password2 = ref('');
const error = ref('')
const books = ref([]);
const defaultUser = ref('');

const user = ref({username: "", password: "", name: "", surname: "", gender: "", date: ""});

const managerFactory = ref({factoryId: -1, managerUsername: ''})
const factoryId = ref(route.query.id || -1);



function loadBooks() {
        axios.get('http://localhost:8080/WebShopAppREST/rest/get')
            .then(response => {
                (books.value = response.data)
        });
        defaultUser.value = JSON.parse(localStorage.getItem('userData'));
    }

function addGenderFemale()
{
    this.user.gender = "female";
}

function addGenderMale()
{
    this.user.gender = "male";
}

function RegisterNew(event)
{
    event.preventDefault();
    if(this.password1 === this.password2 && this.password1 !== "" && this.defaultUser === "")
    {
        this.user.password = this.password1;
        axios.post("http://localhost:8080/WebShopAppREST/rest/customer", this.user)
        .then(response => {
            if(response.data != "")
            {
                router.push('/login');
                loadBooks();
            }
            
            
        });
        
    }
    else if(this.password1 === this.password2 && this.password1 !== "" && defaultUser.value.role === "ADMINISTRATOR") {
        this.user.password = this.password1;
        axios.post("http://localhost:8080/WebShopAppREST/rest/manager", this.user)
        .then(response => {
            if(response.data != "")
            {
                if(factoryId !== -1)
                {
                    this.managerFactory.factoryId = factoryId;
                    this.managerFactory.managerUsername = this.user.username;
                    axios.post("http://localhost:8080/WebShopAppREST/rest/editManager", this.managerFactory)
                }
                    
                loadBooks();
                router.push('/')
            }
            
            
        });

    }
    else if(this.password1 === this.password2 && this.password1 !== "" && defaultUser.value.role === "MANAGER") {
        this.user.password = this.password1;
        axios.post("http://localhost:8080/WebShopAppREST/rest/worker", this.user)
        .then(response => {
            if(response.data != "")
            {
                router.push('/')
                loadBooks();
            }
            
            
        });

    }

    else {
        error.value = "Password is not correct.";
    }


}


</script>

<style scoped>

.form {
  margin: 0 auto;
  max-width: 540px;
  background: white;
  text-align: left;
  border-radius: 10px;
  padding: 20px; /* Add padding for better appearance */
}

.form label {
  color: #aaa;
  display: inline-block;
  margin: 13px 0 10px;
  font-size: 0.8em;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: bold;
}

.form input {
  padding: 10px 6px;
  box-sizing: border-box;
  border: none;
  border-bottom: 1px solid #ddd;
  color: #555;
  display: block;
  width: 100%;
}

.form input[type="radio"] {
  display: inline-block;
  width: 30px;
  margin: 30px 10px 0 0;
  position: relative;
  top: 2px;
}

.form > .submit {
  padding: 0.5rem;
  border: none;
  border-radius: 10px;
  background-color: #5a086a;
  color: white;
  cursor: pointer;
  margin-top: 30px;
  width: 100%;
  height: 40px;
  font-size: 0.8em;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: bold;
}

.form > .submit:hover {
  background-color: #0056b3;
}

p, a {
  display: block;
}


</style>
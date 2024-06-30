<template>
    <div>
        <h1>
     {{ !defaultUser ? 'Register' : (defaultUser.role === 'ADMINISTRATOR' ? 'Create Manager' : (defaultUser.role === 'MANAGER' ? 'Create Worker' : '')) }}
        </h1>
        <form class = "form" v-on:submit="RegisterNew($event)">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id = "username" v-model="user.username" :class = "{'error':userValid.username === ''}">
            </div>
            <div class="form-group">
                <label for="password1">Password:</label>
                <input type="password" id = "password1" v-model="password1" :class = "{'error':userValid.password === ''}">
            </div>
            <div class="form-group">
                <label for="password2">Password:</label>
                <input type="password" id = "password2" v-model="password2" :class = "{'error':userValid.password === ''}">
            </div>
            <div class="form-group">
                <label for="name">Firstname:</label>
                <input type="text" id="name"  v-model="user.name" :class = "{'error':userValid.name === ''}">
            </div>
            <div class="form-group">
                <label for="surname">Surname:</label>
                <input type="text" id="surname"  v-model="user.surname" :class = "{'error':userValid.surname === ''}">
            </div>
            <div class="form-group">
                <label for="gender">Gender:</label>
                <input type="radio" id="male" name="gender" value="male" v-model="male" @click="addGenderMale()">
                <label for="male" id="malelabel">Male</label>
                <input type="radio" id="female" name="gender" value="female" v-model="female" @click="addGenderFemale()">
                <label for="female">Female</label>
            </div>

            <div class="form-group">
                <label for="birthdate">Birthdate:</label>
                <input type="date" id="birthdate" v-model="user.date" :class = "{'error':userValid.date === ''}">
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
const usernameData = ref(localStorage.getItem('userData'));
const workerFactory = ref('');


const user = ref({username: "", password: "", name: "", surname: "", gender: "", date: ""});
const userValid = ref({username: "a", password: "1", name: "a", surname: "a", gender: "male", date: "2024-01-01"});

const managerFactory = ref({factoryId: -1, managerUsername: ''})
const factoryId = ref(route.query.id || -1);



function loadBooks() {
        axios.get('http://localhost:8080/WebShopAppREST/rest/get')
            .then(response => {
                (books.value = response.data)
        });
        axios.get("http://localhost:8080/WebShopAppREST/rest/getLogedUser?username=" + usernameData.value)
    .then(response => {
        defaultUser.value = response.data;
        
    })
    .catch(error => {
      
    });
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
    userValid.value = user.value;
    userValid.value.password = password1;
    if(userValid.value.date === "" || userValid.value.name === "" || userValid.value.password === "" ||
     userValid.value.surname === "" || userValid.value.username === "")
    {
        error.value = "Not all fields are valid.";
    }

    else if(this.password1 === this.password2 && this.password1 !== "" && this.defaultUser === "")
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
                this.managerFactory.factoryId = this.defaultUser.factory.id;
                this.managerFactory.managerUsername = this.user.username;
                axios.post("http://localhost:8080/WebShopAppREST/rest/editManager", this.managerFactory).then(
                    response => {
                        router.push('/')
                        loadBooks();
                    }
                )
                
            }
            
            
        });

    }

    else {
        error.value = "Password is not correct.";
    }


}


</script>

<style scoped>

* {
    padding: 0;
    margin: 0;
}

.form {
    margin: 0 auto;
    max-width: 480px;
    background: white;
    text-align: left;
    border-radius: 10px;
    padding: 5px; /* Add padding for better appearance */
}

.form label {
    color: gray;
    margin: 13px 10px 10px 0; /* Adjust margin to add space between label and input */
    font-size: 0.95em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;
    flex: 0 0 150px; /* Adjust the width of the label */
}

.form .form-group {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
}

.form input {
    padding: 3px 6px;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #aeaeae;
    color: #555;
    width: 100%;
}

.form input[type="radio"] {
    display: inline-block;
    width: 30px;
    margin: 0 8px 0 0;
    position: relative;
    top: 1px;
}

.form > .submit {
    padding: 0.5rem;
    border: none;
    border-radius: 10px;
    background-color: #5a086a;
    color: white;
    cursor: pointer;
    margin-top: 20px;
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

h1 {
    text-align: center;
    margin-top: 15px;
    margin-bottom: 10px;
    color: #5a086a;
    font-weight: 300;
}
.form .error{
    border-bottom: 2px solid red;
}


</style>
<template>
    <div>
        <h1>Login</h1>
        <form class="loginForm" @submit.prevent="login">
            <div>
                <label for="username">Username:</label>
                <input type="text" id="username" v-model="username" required>
            </div>
            <div>
                <label for="passord">Password:</label>
                <input type="text" id="password" v-model="password" required>
            </div>
            <button type="submit" class="submit">Login</button>
        </form>
        <p>{{errorMessage}}</p>
        <a href="" @click="Register($event)">Register</a>
        <a href="" @click.prevent="getUser()">User</a>
    </div>
</template>

<script setup>
import axios from "axios";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";

const username = ref('');
const password = ref('');
const errorMessage = ref('');
const user = ref('');
const router = useRouter();


const login = () => {
    axios.post('http://localhost:8080/WebShopAppREST/rest/login1', {username: username.value, password: password.value})
    .then(response => {
        errorMessage.value = 'Succesfuly loged into account.';
        console.log(response.data);
        //localStorage.setItem('token', response.data);
        user.value = response.data;
         localStorage.setItem('userData', JSON.stringify(response.data));
    })
    .catch(error => {
        errorMessage.value = 'This user does not exists. Please register if you do not have account.';
        console.error(error);
        localStorage.setItem('userData', JSON.stringify(""));
    });
    
}

function getUser(){
    const userData = JSON.parse(localStorage.getItem('userData'));
    console.log(userData);

}

function Register(event)
{
    event.preventDefault();
    router.push("/register");
}

</script>



<style scoped>
.loginForm {
    margin: 0 auto;
    max-width: 420px;
    background: white;
    text-align: left;
    border-radius: 10px;
    padding: 40px;
}


.loginForm label {
    color: #aaa;
    display: inline-block;
    margin: 25px 0 15px;
    font-size: 0.8em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;
}

.loginForm input {
    padding: 10px 6px;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #ddd;
    color: #555;
    display: block;
    width: 100%;
}

.loginForm > .submit {
    padding: 0.5rem;
    border: none;
    border-radius: 10px;
    background-color: #5a086a;
    color: white;
    cursor: pointer;
    margin-top: 50px;
    width: 100%;
    height: 40px;
    font-size: 0.8em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;
}

.loginForm > .submit:hover {
    background-color: #0056b3;
}


p, a {
    display: block;
}
</style>
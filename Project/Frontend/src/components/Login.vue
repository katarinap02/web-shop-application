<template>
    <div>
        <h1>Log in</h1>
        <form class="loginForm" @submit.prevent="login">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" v-model="username" required>
            </div>
            <div class="form-group">
                <label for="passord">Password:</label>
                <input type="password" id="password" v-model="password" required>
            </div>
            <button type="submit" class="submit">Login</button>
        </form>
        <p>{{errorMessage}}</p>
        
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
    axios.post('http://localhost:8080/WebShopAppREST/rest/login', {username: username.value, password: password.value})
    .then(response => {
        errorMessage.value = 'Succesfuly loged into account.';
        console.log(username.value);
        localStorage.setItem('userData', username.value);
        router.push("/")
        
    })
    .catch(error => {
    if (error.response) {
        // Server responded with a status code and response data
        const responseMessage = error.response.data;
        
        if (responseMessage === "Invalid username and/or password") {
            errorMessage.value = 'This user does not exists. Please register if you do not have account';
        } else if (responseMessage === "This user is bloked") {
            errorMessage.value = 'This user is blocked.';
        } else {
            errorMessage.value = 'An unknown error occurred. Please try again.';
        }
    } else {
        // No response from the server (network error, etc.)
        errorMessage.value = 'Network error. Please check your internet connection and try again.';
    }
        console.error(error);
        localStorage.setItem('userData', "");
    });
    
}

function getUser(){
    const userData = JSON.parse(localStorage.getItem('userData'));
    console.log(userData);

}

function goHome()
{
    router.push("/");
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
    padding: 20px;
}

h1 {
    text-align: center;
    margin-top: 15px;
    color: #5a086a;
    font-weight: 300;
}


.loginForm label {
    color: gray;
    display: inline-block;
    margin: 25px 15px 15px;
    font-size: 0.95em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;
}

.loginForm input {
    padding: 10px 6px;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #aeaeae;
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
    margin-top: 30px;
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

.loginForm .form-group {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
}

</style>
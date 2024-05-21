<template>
    <div>
        <h1>Register</h1>
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
            <button type="submit" class="submit">Register</button>
        </form>
        <table>
        <table id="tabelaKnjiga">
				<thead style="background-color: aqua;">
					<tr>
						<th style="font-weight: bold;">Username</th>
						<th style="font-weight: bold;">Password</th>
                        <th style="font-weight: bold;">Date</th>
					</tr>
				</thead>

				<tr v-for="b in books">
					<td>{{ b.username }}</td>
					<td>{{ b.gender }}</td>
					<td>{{ b.birthDate }}</td>
				</tr>
			</table>
    </table>
    <p>{{ error }}</p>
    </div>
  
</template>

<script setup>

import axios from "axios";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
onMounted(() => {
    loadBooks();
})


const male = ref(false);
const female = ref(true);

const password1 = ref('');
const password2 = ref('');
const error = ref('')
const books = ref([]);

const user = ref({username: "", password: "", name: "", lastname: "", gender: "", date: ""});

function loadBooks() {
        axios.get('http://localhost:8080/WebShopAppREST/rest/get')
            .then(response => {
                (books.value = response.data)
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
    if(this.password1 === this.password2 && this.password1 !== "")
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
    else {
        error.value = "Password is not correct.";
    }


}


</script>

<style scoped>



.form {
    margin: 0 auto;
    max-width: 560px;
    background: white;
    text-align: left;
    border-radius: 10px;
}

.form label {
    color: #aaa;
    display: inline-block;
    margin: 18px 0 13px;
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

.form input[type = "radio"]{
    display: inline-block;
    width: 30px;
    margin: 30px 10px 0 0;
    position: relative
    top 2px;
    
}



.form > .submit {
    padding: 0.5rem;
    border: none;
    border-radius: 10px;
    background-color: #007bff;
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

.form > .submit:hover {
    background-color: #0056b3;
}


p, a {
    display: block;
}


</style>
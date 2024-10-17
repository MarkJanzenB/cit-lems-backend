<img src="https://i.imgur.com/d4mV2b3.png" width="350">

# LEMS backend
## Prerequisite
1. Your IDE should run on JDK 17
2. MySQL should be running on services
3. Manually create a schema(database) named lemsdb
4. Check application. properties file if database credentials is correct
5. If you are using react, install axios
   ```npm i axios```

## How to use LEMS backend
1. Create a database/schema on workbench or wherever you prefer and name it lemsdb
2. In LEMS backend, go to src/main/resources
3. Open application.properties
4. Change credentials (if yours is different)
5. To run, go to src/main/java
6. Under com.capstone.LEMS open LemsApplication.java
7. Run as Java Application

## APIs
### Registration
`http://localhost:8080/user/register`<br/>
**How to use**<br/>
Store user inputs in an object<br/>
Example using React:
```
const [credentials, setCredentials] = useState({
  fname: 'John',
  lname: 'Doe',
  password: 'john123',
  idnum: '11-1111-111',
  email: 'johndoe@email.com',
  acctype: 'LA'
});
```
> [!CAUTION]
> Make sure that the keys are the same as the column name.

Pass the object using axios or anything<br/>
Example using axios:
```
const register = async (credentials) => {
    try {
      const response = await axios.post("http://localhost:8080/user/register", credentials);
      return response.data;
    } catch (error) {
      console.error("Error:", error);
    }
};

//pass an event in this function if you are using form's submit button/funciton
const handleSubmit = async (e) => {
e.preventDefault(); // Prevent default form submission behavior like auto refresh
        try {
            const result = await register(credentials);
            console.log(result); //displays in console if register was successful
        } catch (e) {
            console.error("Error submitting form: ", e);
        }
    };
```

### Login
`http://localhost:8080/user/login?idnum=${idnum}&password=${password}`<br/>
**How to use**<br/>
Example using axios:
```
const checkLoginCredentials = async(username, password) => {
  try {
    const response = await axios.get(`http://localhost:8080/user/login?idnum=${idnum}&password=${password}`);
    return response.data;
  }catch (error) {
    console.error("Error:", error);
  }
}

//pass an event in this function if you are using form's submit button/funciton
const handleSubmit = async (e) => {
e.preventDefault(); // Prevent default form submission behavior like auto refresh
        try {
            const result = await checkLoginCredentials(username, password);
            console.log(result); //displays in console if register was successful
        } catch (e) {
            console.error("Error submitting form: ", e);
        }
    };
```


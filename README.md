<img src="https://i.imgur.com/d4mV2b3.png" width="350">

# LEMS backend
## Prerequisite
1. Your IDE should run on JDK 17
2. MySQL should be running on services
3. Manually create a schema(database) named lemsdb
4. Check application. properties file if database credentials is correct

## How to clone and use LEMS backend
1. **Clone using Eclipse IDE** (*you can use any IDE you want as long as you know how to clone*)
   - Click file
   - Click import
   - Under Git folder click Projects from Git (with smart import)
   - Click next
   - Click Clone URI
   - Enter the URI(git link for this repo)
   - Click next
   - Make sure main and testing branches are checked
   - Click next
   - (Optional) Select a different directory
   - Initial branch: testing
   - Click next
   - Click finish
   - Wait for it to load all the files (look at the bottom right)
  
2. **Running**
   - Create a database/schema on workbench or wherever you prefer and name it lemsdb
   - In LEMS backend, go to src/main/resources
   - Open application.properties
   - Change credentials (if yours is different)
   - To run, go to src/main/java
   - Under com.capstone.LEMS open LemsApplication.java
   - Run as Java Application

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
```


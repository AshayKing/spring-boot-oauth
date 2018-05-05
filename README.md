# spring-boot-oauth
Practice of Spring boot oauth using JWT

### run example
<code>mvn spring-boot:run</code>

### Request for token 
<code>
curl trusted-app:secret@localhost:8080/oauth/token -d "grant_type=password&username=user&password=password"
</code>

### Access API 
<code>
curl -H "Authorization: Bearer [ACCESS_TOKEN]" localhost:8080/api/user
</code>



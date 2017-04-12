# shiro-django-auth
Apache Shiro authentication with Django user id and password
## Build
```bash
$ mvn package
```
If you are using Apache Zeppelin, copy extracted `.jar` file to `[your Zeppelin folder]/lib/`.
## Usage
In `main` section of configuration file `shiro.ini`, add following lines.
```
...
[main]
ps = com.ridi.shiro.DjangoPasswordService
pm = org.apache.shiro.authc.credential.PasswordMatcher
pm.passwordService = $ps
jdbcRealm.credentialsMatcher = $pm
...
```
You may configure `jdbcRealm` to connect database for Django user authentication.

## Supported password hashing algorithms
- pbkdf2_sha256

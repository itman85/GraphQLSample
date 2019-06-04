# GraphQLSample
This android app will list all the current trending repositories in GitHub by calling Github GraphQL API endpoint using Apollo Android GraphQL library.
 - Install apollo CLI
 $ npm install -g apollo
 $ apollo (-v|--version|version)
 //download github graphql schema, it will be download into current folder in terminal
 $ apollo schema:download --endpoint=https://api.github.com/graphql --header="Authorization: Bearer <token>"
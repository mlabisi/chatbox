# chatbox
A TCP Client-Server chat client.	

## Table of Contents
- [Running the Chatbox](#running-the-server-)
  * [Step One - Get the Source Code](#step-one---get-the-source-code)
  * [Step Two - Install Dependencies](#step-two---install-dependencies)
  * [Step Three - Run The Server Locally](#step-three---run-the-server-locally)


## Running the Server 💻
### Step One - Get the Source Code
Clone this repository by opening up the command line and running the following:
```shell script
mkdir chatbox
git clone https://github.com/mlabisi/chatbox.git
cd chatbox
```

### Step Two - Install Dependencies
Make sure you have the following libraries installed for this project
* [JUnit v4.13](https://search.maven.org/search?q=g:junit%20AND%20a:junit)
* [Mockito v3.3.3](https://search.maven.org/search?q=g:org.mockito%20AND%20a:mockito-core&core=gav)

### Step Three - Run The Server Locally
Now that you've installed the dependencies, you can run the server locally! To do so, go ahead run the `ChatServer::main` method.
Once the server is running, run the server by running `ChatClient::main` method.  Check the console for program info and warnings.

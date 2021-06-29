//This is the Server side implementation of TCP Chat

#include<string.h>
#include<sys/socket.h>                    
#include<netinet/in.h>                    
#include<unistd.h>
#include<stdio.h>
#include <stdbool.h>


int TCP_ChatServer(int client_desc)
{
	const int BUFFER_SIZE = 4096;
	char msg[BUFFER_SIZE];
	int  msg_len = 0;

	while((msg_len = read(client_desc, msg, BUFFER_SIZE)) != 0) 
	{
		printf("%s %d %s","[User",client_desc,"] Msg Received :");
		fflush(stdout);
		
		write(fileno(stdout), msg, msg_len);

		if(msg[0] == 'b' && msg[1] == 'y' && msg[2] == 'e')
			return 0;
	
		printf("%s %d %s","[User",client_desc,"] Enter Response :");
		
		fflush(stdout);
		
		
		msg_len = read(fileno(stdin), msg, BUFFER_SIZE);
		
		write(client_desc, msg, msg_len);
		
		if(msg[0] == 'b' && msg[1] == 'y' && msg[2] == 'e')
			return 0;
	}
	
	return 0;
}

int main()
{
    //Create Socket
	int  server_desc = socket(AF_INET,SOCK_STREAM,0);
 
    //Create and Fill Address Structure for this Server
	struct  sockaddr_in server_addr;
	server_addr.sin_family      = AF_INET;    //Address Family (AF_INET, AF_INET6, AF_LOCAL, ...)
    server_addr.sin_addr.s_addr = INADDR_ANY; //Internet Address (INADDR_ANY-> Accept connection at any IP Address)
    server_addr.sin_port        = htons(9000);//Port Number (htons -> h.HOST t.TO n.NETWORK s.SHORT , Ensures proper byte ordering)

	//Bind Socket Descriptor and Address Structure together
	int result = bind(server_desc,  (struct sockaddr*) &server_addr, sizeof(server_addr));

	//Start Listioning (Tell kernel to accept connections directed towards this socket) (Puts socket into passive mode)
	listen(server_desc,4);                     
		                                   
		   


	//Server Loop
	bool RunServer = true;
	while(RunServer)
	{
		//Accept a Connection (Puts process in sleep mode if Connection Queue is Empty)
		int client_desc;
		client_desc = accept(server_desc,NULL,NULL);        //Listening Socket
	
		//Create Child Process to handle connection
		int pid = fork();
		
		if(pid > 0)                              //Parent Process
		{
			//Close Client Socket
			close(client_desc);
			continue;
		}
		else
		if(pid == 0)                             //Child Process
		{
			//Close Listening Socket
			close(server_desc);

			TCP_ChatServer(client_desc);

			//Close Connection
			close(client_desc);
			break;                               //Work Done! Exit Child Process.
		}
		else
		{
			printf("%s ","fork() Error!!!");
			break;
		}
	}

							  	
	return 0;
}

//A TCP Chat Client 

//#include"Common.h"
#include<string.h>
#include<time.h>
#include<stdio.h>
#include<unistd.h>
#include<stdlib.h>
#include<string.h>
#include<signal.h>
#include<errno.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<sys/socket.h>
#include<sys/wait.h>
#include <stdbool.h>


int TCP_ChatClient(int server_desc)
{
	const int BUFFER_SIZE = 4096;
	char msg[BUFFER_SIZE];
	int  len = 0;

	while(true) 
	{
	//	cout<<"\n Input : ";fflush(stdout);
	printf("%s ","Input:"); fflush(stdout);
		len = read(fileno(stdin), msg, BUFFER_SIZE);
		//CheckError(len, "read()");

		if(len == 0) //EOF
			return 0;

		len = write(server_desc, msg, len);
		//CheckError(len, "write()");
		
		if(msg[0] == 'b' && msg[1] == 'y' && msg[2] == 'e')
			return 0;
		
		len = read(server_desc, msg, BUFFER_SIZE);
		//CheckError(len, "read()");
	
		//cout<<" Responce From Server :"; fflush(stdout);
		
		printf("%s ","Response from server :"); fflush(stdout);
		len = write(fileno(stdout), msg, len);
		//CheckError(len, "write()");
		
		if(msg[0] == 'b' && msg[1] == 'y' && msg[2] == 'e')
			return 0;
	
	}
	return 0;
}

int main()
{
	int sock = socket(AF_INET, SOCK_STREAM, 0);
	//CheckError(sock, "socket()");

	struct sockaddr_in  server_addr;
	server_addr.sin_family        = AF_INET;
	server_addr.sin_addr.s_addr   = inet_addr("127.0.0.1");
	server_addr.sin_port          = htons(9000);

	int result = connect(sock, (struct sockaddr*)&server_addr, sizeof(server_addr));
	//CheckError(result, "connect()");
	
	TCP_ChatClient(sock);


	close(sock);
	return 0;
}

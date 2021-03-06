//This is the client side implementation of UDP Chat

#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<netdb.h>
#include<stdio.h>
#include<unistd.h>
#include<string.h>
#include<sys/time.h>


#define MAX_MSG 100
#define SERVER_ADDR "127.0.0.1"
#define SERVER_PORT 1500

int main()
{
int sd,rc,n,templen;
struct sockaddr x;
struct sockaddr_in cliAddr,tempAddr,remoteServAddr;
char msg[MAX_MSG];

bzero((char *)&remoteServAddr,sizeof(remoteServAddr));
remoteServAddr.sin_family=AF_INET;
remoteServAddr.sin_addr.s_addr=inet_addr(SERVER_ADDR);
remoteServAddr.sin_port=htons(SERVER_PORT);

sd=socket(AF_INET,SOCK_DGRAM,0);
printf("datagram socket craeted successfully\n");

do{
//send data to server

printf("Enter data to send:");
scanf("%s",msg);

sendto(sd,msg,strlen(msg)+1,0,(struct sockaddr *)&remoteServAddr,sizeof(remoteServAddr));

}while(strcmp(msg,"quit"));

close(sd);

}

#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<netdb.h>
#include<stdio.h>
#include<unistd.h>
#include<string.h>

#define MAX_MSG 100
#define SERVER_ADDR "127.0.0.1"
#define SERVER_PORT 1500

int main()
{
int sd,rc,n,cliLen;
struct sockaddr x;
struct sockaddr_in cliAddr,servAddr;
char msg[MAX_MSG];

printf("\n sockaddr %ld",sizeof(x));
printf("\n long %ld",sizeof(long));
printf("\nint %ld",sizeof(int));
printf("\n sockaddr_in %ld",sizeof(cliAddr));
printf("\n short %ld\n",sizeof(short));

// build server address structure/*

bzero((char *)&servAddr,sizeof(servAddr));
servAddr.sin_family=AF_INET;
servAddr.sin_addr.s_addr=inet_addr(SERVER_ADDR);
servAddr.sin_port=htons(SERVER_PORT);
//CREATE DATAGRAM SOCKET

sd=socket(AF_INET,SOCK_DGRAM,0);
printf("datagram socket craeted successfully\n");
//BIND LOCAL PORT NUMBER


bind(sd,(struct sockaddr*)&servAddr,sizeof(servAddr));
printf("successfully bind local address\n");

printf("waiting for data on port UDP %u\n",SERVER_PORT);

while(1)
{
//init buffer

memset(msg,0x0,MAX_MSG);

//Receive data from client

cliLen=sizeof(cliAddr);

n=recvfrom(sd,msg,MAX_MSG,0,(struct sockaddr *) &cliAddr,&cliLen);

printf("from %s: UDP port %u: %s \n",inet_ntoa(cliAddr.sin_addr),ntohs(cliAddr.sin_port),msg);
printf("from %ld: UDP port %ld,in network byte ordering : %s \n",cliAddr.sin_addr,cliAddr.sin_port,msg);

}

return 0;

}

































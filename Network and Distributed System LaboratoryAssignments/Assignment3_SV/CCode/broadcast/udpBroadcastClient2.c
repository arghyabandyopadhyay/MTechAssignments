//This is the client side implementation of UDP Broadcast service for receiving message passed from the server

#include <stdio.h> 
#include <stdlib.h> 
#include <errno.h> 
#include <string.h> 
#include <sys/types.h> 
#include <netinet/in.h> 
#include <sys/socket.h> 
#include <sys/wait.h> 
#include <sys/time.h> 
#include <sys/unistd.h> 
#include <arpa/inet.h>

#define MYPORT 5000    /* the port users will be sending to */

#define MAXBUFLEN 100

int main()
{
    int sockfd;
    struct sockaddr_in my_addr;    /* my address information */
    struct sockaddr_in their_addr; /* connector's address information */
    int addr_len, numbytes;
    char buf[MAXBUFLEN];
   int option = 1;

    if ((sockfd = socket(AF_INET, SOCK_DGRAM, 0)) == -1) {
        perror("socket");
        exit(1);
    }
   else
	  printf(" \n The socket got sockfd=%d \n ",sockfd);

   setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &option, sizeof(option));

    my_addr.sin_family = AF_INET;         /* host byte order */
    my_addr.sin_port = htons(MYPORT);     /* short, network byte order */
    my_addr.sin_addr.s_addr = INADDR_ANY; /* auto-fill with my IP */
    bzero(&(my_addr.sin_zero), 8);        /* zero the rest of the struct */


    if (bind(sockfd, (struct sockaddr *)&my_addr, sizeof(struct sockaddr)) /* The bind command makes the ability to wait for messages */
                                                                   == -1) {
        perror("bind");
        exit(1);
    }

   printf("Wait for packet \n");

    addr_len = sizeof(struct sockaddr);


   if ((numbytes=recvfrom(sockfd, buf, MAXBUFLEN, 0, \
                        (struct sockaddr *)&their_addr, &addr_len)) == -1) {
                    perror("recvfrom");
                    exit(1);
            }

   	printf("got packet from %s ",inet_ntoa(their_addr.sin_addr));
   	printf("packet is %d bytes long ",numbytes);
   	buf[numbytes] = '\0'; 
   	printf("packet contains \"%s\"\n",buf);


    close(sockfd);

   return 0;
}

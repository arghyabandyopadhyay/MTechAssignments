/***************************************************************************/
 /*    This sends a BROADCAST Limited message                               */
 /*                                                                         */
 /*    The program send message given as argument to the port given as      */
 /*        second argument.                                                 */
 /*                                                                         */
 /*                                                                         */
 /*                                                                         */
 /*                                                                         */
 /*                                                                         */
 /*    Eddie Aronovich                                                      */
 /***************************************************************************/

    #include <stdio.h>
    #include <stdlib.h>
    #include <errno.h>
    #include <string.h>
    #include <sys/types.h>
    #include <netinet/in.h>
    #include <netdb.h>
    #include <sys/socket.h>
    #include <sys/wait.h>
    #include <arpa/inet.h>

    #define MAXBUFLEN 100    /* the port users will be connecting to */

    int main(int argc, char *argv[])
    {
        int sockfd;
        struct sockaddr_in their_addr; 	/* connector's address information */
        struct sockaddr_in my_addr; 	/* connector's address information */
        int numbytes;
	int optval;			/*Used to build the options for the broadcast */
	int optlen;
        char buf[MAXBUFLEN];            /*The buffer that we read / write each time   */
	int addr_len;			/* Address length for the network functions
                                                                 that require that    */
	unsigned long int net_id;	/*The network id		*/
	long int	  host_id;	/*The hpst id in the network	*/


        if (argc != 3) {
            fprintf(stderr,"usage: %s message port\n",argv[1]);
            exit(1);
        }

        if ((sockfd = socket(AF_INET, SOCK_DGRAM, 0)) == -1) { 	/* The socket should be changed to broadcast 	*/
								/* This part demands root permissions		*/
            perror("socket");
            exit(1);
        }

	optval=1; /*Prepare the options of the socket for Broadcast */
 	optlen=sizeof(int); 

	if(setsockopt(sockfd,SOL_SOCKET,SO_BROADCAST,(char *) &optval,optlen)){ 
	    perror("Error setting socket to BROADCAST mode"); 
  	    exit(1); 
 	} 

        their_addr.sin_family = AF_INET;     				/* Protocol family - host byte order */
	their_addr.sin_port=htons((unsigned short) atoi(argv[2])); 	/* port - short, network byte order */
 	their_addr.sin_addr.s_addr=htonl(INADDR_BROADCAST); 		/* send to all */ 	
 	/*their_addr.sin_addr.s_addr=inet_addr("enter here the IP addres in dot notation"); */	/* send to all */ 	
        bzero(&(their_addr.sin_zero), 8);     /* zero the rest of the struct */

        if ((numbytes=sendto(sockfd, argv[1], strlen(argv[1]), 0, \
             (struct sockaddr *)&their_addr, sizeof(struct sockaddr))) == -1) {
            perror("sendto");
            exit(1);
        }

        printf("sent %d bytes to %s\n",numbytes,inet_ntoa(their_addr.sin_addr));
	
        close(sockfd);

        return 0;
    }

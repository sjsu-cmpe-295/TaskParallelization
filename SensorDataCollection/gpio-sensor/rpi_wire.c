#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <curl/curl.h>

#define temp_sensor  "/sys/bus/w1/devices/10-000800c4ba57/w1_slave"
function_pt(void *ptr, size_t size, size_t nmemb, void *stream){
    printf("%d", atoi(ptr));
}

int main(int argc, char ** argv)
{
	while(1) {

		sleep(5);
		int fd =0,n=0,i=0;
		CURL *curl;
		CURLcode res;
		
		// Open the Port. We want read/write, no "controlling tty" status, and open it no matter what state DCD is in
		fd = open(temp_sensor, O_RDONLY);
		if (fd == -1)
		{
			perror("open_port: Unable to open /sys/bus/w1/devices/10-000800c4ba57/w1_slave - ");
			return(-1);
		}

		// Read up to 255 characters from the port if they are there
		char buf[256];
		char *pch ;
		char newbuf[128];
		unsigned int temperature =0;
		n = read(fd, (void*)buf, 255);
		if (n < 0)
		{
			perror("Read failed - ");
			return -1;
		}
		else if (n == 0)
		{
			printf("No data on port\n");
		}
		else
		{
			buf[n] = '\0';
		}
		printf("%s\n",buf);
		pch = strstr (buf,"t=");
		bzero(newbuf,0);
		memcpy(newbuf,pch,7);
		printf("%s\n",&newbuf[2]);

		temperature = atoi(&newbuf[2]);
		
	 
		/* In windows, this will init the winsock stuff */ 
		curl_global_init(CURL_GLOBAL_ALL);

		/* get a curl handle */ 
		curl = curl_easy_init();
		if(curl)
		{	
			char tempStr[256] = "";
			// Amazon AWS instance where node-server is running to receive temperature data
			sprintf(tempStr, "http://54.183.182.111:3000/raspberryApi?sensorId=1&temperature=%d",temperature);
			//strcat(tempStr, newbuf);
			printf("New String %s\n", tempStr);
			curl_easy_setopt(curl, CURLOPT_URL, tempStr);
		    /* example.com is redirected, so we tell libcurl to follow redirection */ 
		    curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);

		    //Set response function
		    //curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, function_pt);
		 
		    /* Perform the request, res will get the return code */ 
		    res = curl_easy_perform(curl);
		    /* Check for errors */ 
		    if(res != CURLE_OK)
		      fprintf(stderr, "curl_easy_perform() failed: %s\n",
		              curl_easy_strerror(res));
		  	else {
		  		printf("success");
		  	}
		 
		    /* always cleanup */ 
		    curl_easy_cleanup(curl);

		}
		
		
	  
		close(fd);
	}
	
	return 0;
}

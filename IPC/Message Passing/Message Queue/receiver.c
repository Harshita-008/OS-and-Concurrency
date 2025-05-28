#include <stdio.h>
#include <mqueue.h>
#include <fcntl.h>
#include <sys/stat.h>

int main() {
    mqd_t mq;
    char buffer[256];

    mq = mq_open("/myqueue", O_RDONLY);
    if (mq == -1) {
        perror("mq_open");
        return 1;
    }

    if (mq_receive(mq, buffer, sizeof(buffer), NULL) == -1) {
        perror("mq_receive");
    } else {
        printf("Received: %s\n", buffer);
    }

    mq_close(mq);
    mq_unlink("/myqueue");  // Clean up
    return 0;
}
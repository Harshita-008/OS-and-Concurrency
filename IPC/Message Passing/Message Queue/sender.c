#include <stdio.h>
#include <mqueue.h>
#include <string.h>
#include <fcntl.h>  // For O_* constants
#include <sys/stat.h>

int main() {
    mqd_t mq;
    char msg[] = "Hello from sender!";
    struct mq_attr attr;

    attr.mq_flags = 0;               // Blocking mode
    attr.mq_maxmsg = 10;             // Max messages
    attr.mq_msgsize = 256;           // Max size per message
    attr.mq_curmsgs = 0;

    mq = mq_open("/myqueue", O_CREAT | O_WRONLY, 0644, &attr);
    if (mq == -1) {
        perror("mq_open");
        return 1;
    }

    if (mq_send(mq, msg, strlen(msg) + 1, 0) == -1) {
        perror("mq_send");
    } else {
        printf("Sent: %s\n", msg);
    }

    mq_close(mq);
    return 0;
}
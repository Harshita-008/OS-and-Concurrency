#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <unistd.h>

int main() {
    const char *name = "/my_shm";    
    const int SIZE = 4096;

    // Open existing shared memory object
    int shm_fd = shm_open(name, O_RDONLY, 0666);
    if (shm_fd == -1) {
        perror("shm_open");
        exit(1);
    }

    // Memory map
    void *ptr = mmap(0, SIZE, PROT_READ, MAP_SHARED, shm_fd, 0);
    if (ptr == MAP_FAILED) {
        perror("mmap");
        exit(1);
    }

    // Read from shared memory
    printf("🧒 Reader read: %s\n", (char *)ptr);

    // Cleanup
    shm_unlink(name); // Remove shared memory
    close(shm_fd);
    return 0;
}
/* 
 * File:   Lexical.c
 * Author: ravs
 *
 * Created on August 27, 2010, 7:08 AM
 */

#include <stdio.h>
#include <stdlib.h>

/*
 * 
 */
int main(int argc, char** argv) {

    FILE* source;
    char ch;
    int state;
    int chRead = 0;

    argc = 2;
    argv[1] = "Hello.c";
    
    if (argc<2) {
        printf("No file name provided. \n");
        exit(1);
    }
    if (source = fopen(argv[1], "r") == NULL) {
        printf("Cannot open %s \n", argv[1]);
        exit(2);
    }

    ch = getc(source);
    chRead++;
    state = 0;

    while (ch!=EOF) {

        switch(state) {
            case 0:
                if (ch == 'i')
                    state = 1;
                else if (ch == 'r')
                    state = 4;
                else
                    state = 10;
                break;
            case 1:
                if (ch == 'n')
                    state = 2;
                else {
                    fseek(source, chRead, SEEK_CUR);
                    continue;
                }

                break;

        }
        ch = getc(source);
        chRead++;
    }
    
    fclose(argv[1]);
    return (EXIT_SUCCESS);
}




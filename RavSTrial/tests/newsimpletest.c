/*
 * File:   newsimpletest.c
 * Author: ravs
 *
 * Created on Jul 15, 2010, 3:16:39 PM
 */

#include <stdio.h>
#include <stdlib.h>

/*
 * Simple C Test Suite
 */

void addLastNode();

void testAddLastNode() {
    addLastNode();
    if(1 /*check result*/) {
        printf("%%TEST_FAILED%% time=0 testname=testAddLastNode (newsimpletest) message=error message sample\n");
    }
}

int main(int argc, char** argv) {
    printf("%%SUITE_STARTING%% newsimpletest\n");
    printf("%%SUITE_STARTED%%\n");

    printf("%%TEST_STARTED%%  testAddLastNode (newsimpletest)\n");
    testAddLastNode();
    printf("%%TEST_FINISHED%% time=0 testAddLastNode (newsimpletest)\n");
    
    printf("%%SUITE_FINISHED%% time=0\n");

    return (EXIT_SUCCESS);
}

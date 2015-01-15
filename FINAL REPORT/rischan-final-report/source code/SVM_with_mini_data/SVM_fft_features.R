#Coded by Rischan Mafrur
#install.packages("e1071")
library("e1071")

setwd("D:/Dropbox/LAB/COURSE/3/ubi/projects/data_training/csv")
#SVM model classifier
feature_data <- read.csv("fft_tot.csv",header = TRUE)
head(feature_data)
x <- subset(feature_data, select=-label)
y <- feature_data$label

#creating training model
svm_model <- svm(x,y,kernel="radial",cost=1, gamma =0.5)
summary(svm_model)

pred <- predict(svm_model,x)
system.time(pred <- predict(svm_model,x))
#Testing of Model Performances
table(pred,y)

#If the result is good, apply that model to testing data set

#Model Result

# y
# pred      agung alvin gde rischan
# agung     255     0   2       5
# alvin       1   234   5       2
# gde         0     6 266      26
# rischan     1     1   1     171

setwd("D:/Dropbox/LAB/COURSE/3/ubi/projects/data_testing/csv")
test_data <- read.csv("fft_tot.csv", header = TRUE)
x1 <- subset(test_data, select=-label)
y1 <- test_data$label

test_pred <- predict(svm_model,x1)
table(test_pred,y1)

#Final Result

# y1
# test_pred agung alvin gde rischan
# agung     261   129 145     152
# alvin       1    91  10      15
# gde         0    26 122      41
# rischan     6     5   6      25



#accuracy <- data.frame(pred,y)

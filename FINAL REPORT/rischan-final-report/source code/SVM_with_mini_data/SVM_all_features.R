#Coded by Rischan Mafrur
#install.packages("e1071")
library("e1071")

setwd("D:/Dropbox/LAB/COURSE/3/ubi/projects/data_training/csv")
#SVM model classifier
feature_data <- read.csv("all_features.csv",header = TRUE)
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
# agung     257     0   0       2
# alvin       0   238   0       0
# gde         0     2 270      12
# rischan     0     1   4     190

setwd("D:/Dropbox/LAB/COURSE/3/ubi/projects/data_testing/csv")
test_data <- read.csv("all_features.csv", header = TRUE)
x1 <- subset(test_data, select=-label)
y1 <- test_data$label

test_pred <- predict(svm_model,x1)
table(test_pred,y1)

#Final Result

# y1
# test_pred agung alvin gde rischan
# agung     266   151 185     161
# alvin       0    53   8      12
# gde         2    44  82      31
# rischan     0     3   8      29



#accuracy <- data.frame(pred,y)

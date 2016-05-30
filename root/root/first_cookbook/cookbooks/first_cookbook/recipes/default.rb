#
# Cookbook Name:: first_cookbook
# Recipe:: default
#
# Copyright (c) 2016 The Authors, All Rights Reserved.
file "#{ENV['HOME']}/test.txt" do
	content 'This file was created by chef'
end